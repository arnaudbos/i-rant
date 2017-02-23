---
title: 2D Transformations with Android and Java
date: 2017-02-07T02:30:04+01:00
description: I my previous post I've talked about matrices and how they can be used to compute 2D transformations. In this post, I want to talk about how to apply what we know about matrices in order to perform 2D transformations, first using Java AWT and then with the Android SDK.
tags: ["java","android","matrix"]
parent: blog
categories: ["java","android"]
seoimage: /img/2d-transformations-android-java/grumpy-rotate.png
highlight: true
math: true
gallery: true
---

I my previous post, ["Matrices for developers"][matrices-for-developers], I've
talked about matrices and how they can be used to compute 2D transformations.

In this post, I want to talk about how to apply what we know about matrices
in order to perform 2D transformations, first using Java AWT and then with the
Android SDK.

## Table of contents

<div id="toc" class="well col-md-12">
  <!-- toc -->
</div>

## Affine transformations with Java

When I was working on the project I mentioned at the beginning of the previous
article, I was constantly moving back and forth between the
[JDK's implementation of affine transformations][jdk-affine-transform] and the
[Android SDK's implementation of matrices][android-matrix].

I find the `java.awt.geom.AffineTransform` class fairly well featured,
but it is a bit ambiguous. Fortunately, the documentation is
good, it's not perfect but at least it's better than Android's one on this
topic as we shall see later.

The Javadoc starts with a reminder of what are 2D affine transformations and a
matrix multiplication pattern to transform coordinates.

<div>
$$
\begin{bmatrix}
x^{\prime}\\
y^{\prime}\\
1
\end{bmatrix}
=
\begin{bmatrix}
m_{00} & m_{01} & m_{02}\\
m_{10} & m_{11} & m_{12}\\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
x\\
y\\
1
\end{bmatrix}
=
\begin{bmatrix}
m_{00} . x + m_{01} . y + m_{02}\\
m_{10} . x + m_{11} . y + m_{12}\\
1
\end{bmatrix}
$$
</div>

That's neat, you have to appreciate the effort there (I mean, is your
Javadoc that great? {{< emoji content=":innocent:" >}}),
and Android's Javadoc doesn't have it so...

The way this pattern is written lets us see a glimpse of implementation details,
right? Those $m\_{00}$, $m\_{01}$ and etc, they (not so) strangely resemble
stringified versions of indexes in a two-dimensional array.

### Ambiguous — Part 1

So what is *"ambiguous"* with this class? Granted it might be a matter of taste,
but if you look at the constructor
`AffineTransform(m00, m10, m01, m11, m02, m12)` and the method
`setTransform(m00, m10, m01, m11, m02, m12)`, they only take 6 input
parameters although the transformation matrices are $3 \times 3$ dimensions
matrices: they have 9 entries.

While it makes perfectly sense to not take as inputs parameters that are fixed
in the context of 2D affine transformations (`0, 0, 1`), I find it disturbing.

More disturbing perhaps, is the ordering of those parameters.

If you make the parallel between those and our $a\_{11}$, $a\_{12}$, etc. from
the previous post, you can notice that the reading direction is not the same.  
With $a\_{11}$, $a\_{12}$, etc., we used to read more *"naturally"* I would say,
like normal english written text: line by line.  
Whereas $m\_{00}$, $m\_{10}$, etc. is reading the matrix column by column.

I'm not saying one is better than the other, just that I'm more familiar with
the first one, and that it's worth pointing at it to clarify the use of this
class. Because the `getMatrix(flatmatrix)` method will fill in an
array containing the entries of the matrix in that specific order.  
Also, `getMatrix` *"Retrieves the 6 specifiable values in the $3 \times 3$
affine transformation matrix"*, which means it will only give you those
$m\_{00}$, $m\_{10}$, etc., entries, not the ones from the third row.

### Well featured but not that much

To understand what I mean, let's try to execute the kind of transformations we
have seen throughout the first part of this series.

<h5>1. Can we translate?</h5>

Yes! We have `translate(tx, ty)`:

> Concatenates this transform with a translation transformation.

We'll see what "concatenates" means in this context in a moment, for now what
we understand is that we have a method to apply a translation transformation.

<h5>2. Can we shear?</h5>

Yes, but only by constants, not by angles, we have `shear(shx, shy)`:

> Concatenates this transform with a shearing transformation.

<h5>3. Can we scale?</h5>

Yes! We have `scale(sx, sy)`:

> Concatenates this transform with a scaling transformation.

<h5>4. Can we reflect?</h5>

Not directly, at least I don't see anything doing a reflexion directly, so we
either have to scale by negative values, or to use
`setTransform(-1, 0, 0, -1, 0, 0)` (for example) manually and then
`concatenate`.

<h5>5. Can we rotate?</h5>

Yes! We have `rotate(theta)`:

> Concatenates this transform with a rotation transformation.

Beware: `theta` here is in radians, not in degrees.

<h5>6. Can we scale on an anchor point?</h5>

No. You will have to compose your transformation as we've done it "by hand"
above, with a combination of `scale(sx, sy)` and `translate(tx, ty)`.

<h5>7. Can we rotate around an anchor point?</h5>

Yes! We have `rotate(theta, anchorx, anchory)`:

> Concatenates this transform with a transform that rotates coordinates around
an anchor point.

<h5>8. Can we transform points?</h5>

Yes! We have several methods available in order to transform points (even
shapes) from their original position to their new coordinates after the
transformation has been applied.

<h5>Why am I not happy with this?</h5>

I am, actually, and there are more methods that allow you to do interesting
stuff with this class.  
I'm just wondering why they decided to implement
`rotate(theta, anchorx, anchory)` but not
`scale(sx, sy, anchorx, anchory)`.

On the other hand, all the methods I've outlined above are quite opinionated.
Why? Because they assume that what you want to do is:

> Concatenates this transform with a transformation

An that's where bad stuff happen.

### Ambiguous — Part 2

All the transformations we've seen in my previous post about matrices are
defined this way:  

<div>
$$
P^{\prime} = \mathbf{T}.P
$$
</div>

Where:

* $P$ is a point
* $P^{\prime}$ is the point where $P$ will land after the transformation
  has been applied
* $\mathbf{T}$ is a transformation matrix
* $\mathbf{T}$ is the product of many transformation matrices, applied
  in the reverse order: that is for transforming by $\mathbf{A}$, then
  $\mathbf{B}$, then $\mathbf{C}$ we have
  $\mathbf{T} = \mathbf{C} . \mathbf{B} . \mathbf{A} $, and reciprocally.

Now, look at the definition of the
`concatenate(AffineTransform Tx)` method:

> Concatenates an AffineTransform Tx to this AffineTransform Cx in the most commonly useful way to provide a new user space that is mapped to the former user space by Tx. Cx is updated to perform the combined transformation. Transforming a point p by the updated transform Cx' is equivalent to first transforming p by Tx and then transforming the result by the original transform Cx like this: Cx'(p) = Cx(Tx(p)) In matrix notation, if this transform Cx is represented by the matrix __[this__] and Tx is represented by the matrix __[Tx__] then this method does the following:
>
> __[this__] = __[this__] x __[Tx__]

In our notation this gives that for transforming by $\mathbf{A}$, then
$\mathbf{B}$, then $\mathbf{C}$ we have:

<div>
$$
\begin{aligned}
\mathbf{this} &=
\left( \left( \mathbf{this} . \mathbf{A} \right) . \mathbf{B} \right) . \mathbf{C}\\\\
\Leftrightarrow
\mathbf{this} &= \mathbf{this} . \mathbf{Tx}
\text{ where }
\mathbf{Tx} = \mathbf{A} . \mathbf{B} . \mathbf{C}
\end{aligned}
$$
</div>

This is very different than:

<div>
$$
\mathbf{this} = \mathbf{Tx} . \mathbf{this}
\text{ where }
\mathbf{Tx} = \mathbf{A} . \mathbf{B} . \mathbf{C}
$$
</div>

Because matrix multiplication is `associative`,
[remember][matrices-for-developers]?  
But matrix multiplication is *also* `non-commutative`, so this will lead to
very different results than what you might expect!

The good news: there's a method `preConcatenate(AffineTransform Tx)` that does
what we want:

> __[this__] = __[Tx__] x __[this__]

The bad news: you won't be able to represent your transformations with the
built-in `translate`, `scale`, `rotate` as is. Because they don't behave the
way you think: they all fall down to the `concatenate` method.  
At least they don't behave the way ***I*** think about transformations, which is
the one I've described in my post about matrices.

Honestly, I don't know what the Javadoc means by *"in the most commonly useful
way to provide a new user space"*. I'm sure it makes sense for some, but I
don't get it.

So how do we use the `AffineTransform` class to chain our transformations the
way we want?  
Fortunately, the class provides us with a bunch of useful static methods that
return new matrices that are ready to use and can be combined by using the
`preConcatenate` method:

* `AffineTransform.getTranslateInstance(tx, ty)`
* `AffineTransform.getRotateInstance(theta)`
* `AffineTransform.getScaleInstance(sx, sy)`
* `AffineTransform.getShearInstance(shx, shy)`

### Demo

Performance aside, here's a class that will 2x zoom at the center of a
rectangle:

```java
package com.arnaudbos.java2d;
// imports stripped

public class AffineTransformZoomExample {
    // code stripped

    private static class ZoomCanvas extends JComponent {

        public void paint(Graphics g) {
            Graphics2D ourGraphics = (Graphics2D) g;

            // code stripped

            // Draw initial object
            ourGraphics.setColor(Color.black);
            ourGraphics.drawRect(100, 100, 100, 100);

            // Create matrix (set to identity by default)
            AffineTransform tx = new AffineTransform();

            // This is not the transformation you're looking for
            tx.translate(-150, -150);
            tx.scale(2, 2);
            tx.translate(150, 150);
            ourGraphics.setTransform(tx);
            ourGraphics.setColor(Color.red);
            ourGraphics.drawRect(100, 100, 100, 100);

            // Reset matrix to identity to clear previous transformations
            tx.setToIdentity();

            // Apply our transformations in order to zoom-in the square
            tx.preConcatenate(AffineTransform.getTranslateInstance(-150, -150));
            tx.preConcatenate(AffineTransform.getScaleInstance(2, 2));
            tx.preConcatenate(AffineTransform.getTranslateInstance(150, 150));
            ourGraphics.setTransform(tx);
            ourGraphics.setColor(Color.green);
            ourGraphics.drawRect(100, 100, 100, 100);
        }
    }
}
```

----

{{< img src="/img/2d-transformations-android-java/java-zoom-at-center.png"
title="Demo of Java 2D zooming: the original square (black), the unexpected transformation (red) and the desired transformation (green)" center="true"
alt="Demo of Java 2D zooming" width="80%">}}

You can
[browse the source of java.awt.geom.AffineTransform][jdk-affine-transform-src]
if you're interested, you'll see all the matrix multiplications performed
the same way as we've seen in my previous post.

As you can see, `translate` and `scale` which use `concatenate`
(aka. "post"-concatenate) under the hood, don't give the result we might
expect.  
On the other hand, manually using `preConcatenate` and `get...Instance`, will.

Let's see how this works on Android.

## Affine transformations with Android

Unlike Oracle, Google's `android.graphics.Matrix` class assumes you already
know your way around matrices. There's no reminders, no details about matrices,
no explanations. And the source code will help but is a little more tricky
to unroll.

You can [browse the source of android.graphics.Matrix][android-matrix-src] and
notice a lot of `oops();` method calls and `native_` invocations but nothing
really helpful.  
The java `Matrix` class is in fact a proxy to the underlying `JNI`
implementation of matrix operations. The real operations are done by the C++
`Matrix` class.

You can find the interface (`Matrix.h`) [here][android-matrix-h]
and implementation (`Matrix.cpp`) [here][android-matrix-cpp]. But this is just
another level of indirection because this class is just a facade on top of the
`SkMatrix` dependency which does all the real work on matrix operations. The
interface (`SkMatrix.h`) can be found [here][skmatrix-h] and the implementation
(`SkMatrix.cpp`) can be found [here][skmatrix-cpp].

Nonetheless, the API is good and well featured, as long as you understand a
few things.

### Construction

Unlike Java, Android provides ways of building matrices that seem more
explicit and straightforward to me.

The first thing we see in the Javadoc is a bunch of constants that are used to
describe each entry in the matrix:

* `int MPERSP_0 = 6`
* `int MPERSP_1 = 7`
* `int MPERSP_2 = 8`
* `int MSCALE_X = 0`
* `int MSCALE_Y = 4`
* `int MSKEW_X = 1`
* `int MSKEW_Y = 3`
* `int MTRANS_X = 2`
* `int MTRANS_Y = 5`

Put this back ordered by their value and now look at:

* `setValues(values)`: *"Copy 9 values from the array into the matrix."*
* `getValues(values)`: *"Copy 9 values from the matrix into the array."*

What we see here is, I think, a more explicit API than the Java one: you are
dealing with a 3x3 dimensions matrix, so you specify/retrieve the 9 entries
that this matrix is composed of.

Granted Java's `AffineTransform` class is named this way for a reason, that
reason being you can only deal with **affine transformations**. Whereas
Android's `Matrix` class can be used to represent projections by playing with
the `MPERSP_0`, `MPERSP_1` and `MPERSP_2` entries (hence their names and the
`isAffine()` method).

### Well featured

Let's do it again.

<h5>1. Can we translate?</h5>

Yes! We have `preTranslate(dx, dy)` and `postTranslate(dx, dy)`:

> Pre/Post-concats the matrix with the specified translation.

<h5>2. Can we shear?</h5>

Yes, but only by constants not by angles, and it's named "skew".  
We have `preSkew(kx, ky)` and  `postSkew(kx, ky)`:

> Pre/Post-concats the matrix with the specified skew.

We also have `preSkew(kx, ky, px, py)` and `postSkew(kx, ky, px, py)` in order
to skew not around the origin, by around a given anchor point. That's nice.

<h5>3. Can we scale?</h5>

Yes! We have `preScale(sx, sy)` and `postScale(sx, sy)`:

> Pre/Post-concats the matrix with the specified scale.

<h5>4. Can we reflect?</h5>

Again, not directly, we can scale by negative values, or we can use
`setValues({-1, 0, 0, 0, -1, 0, 0, 0, 1})` (for example) and then
`postConcat`.

<h5>5. Can we rotate?</h5>

Yes! We have `preRotate(degrees)` and `postRotate(degrees)`:

> Pre/Post-concats the matrix with the specified rotation.

<h5>6. Can we scale on an anchor point?</h5>

Yes! We have `preScale(sx, sy, px, py)` and `postScale(sx, sy, px, py)`:

> Pre/Post-concats the matrix with the specified scale.

<h5>7. Can we rotate around an anchor point?</h5>

Yes! We have `preRotate(degrees, px, py)` and `postRotate(degrees, px, py)`:

> Pre-Post-concats the matrix with the specified rotation.

<h5>8. Can we transform points?</h5>

Also yes! We have several methods available in order to transform points and
shapes from their original position to their new coordinates after the
transformation has been applied.

### Ambiguous

Yes, I like this word...

The API is undeniably well featured, provides `pre` and `post` methods for
the most common transformations, a `setValues` method to create matrices of
any shape, and also `preConcat(Matrix other)` and `postConcat(Matrix other)`.

What do they do?

<h5>preConcat</h5>

> Preconcats the matrix with the specified matrix. M' = M * other

So, if I read correctly, this is equivalent to:


<div>
$$
\mathbf{this} = \mathbf{this} . \mathbf{Tx}
$$
</div>

Wait... in Java's `AffineTransform`, this was the equivalent of the
`concatenate` method...

<h5>postConcat</h5>

> Postconcats the matrix with the specified matrix. M' = other * M

Again, if I read correctly, this is equivalent to:


<div>
$$
\mathbf{this} = \mathbf{Tx} . \mathbf{this}
$$
</div>

Wait... in Java's `AffineTransform`, this was the equivalent of the
`preConcatenate` method...

<h5>WTF dude?</h5>

Exactly. If you don't read the doc, **you're screwed**
{{% emoji content=":poop:" %}}

So who's right?

I've searched a few minutes on the Interwebs and here's what I've found
[from Wikipedia][wiki-matrix-multi]:

> "pre-multiply (or left multiply) $\mathbf{A}$ by $\mathbf{B}$" means
> $\mathbf{B}.\mathbf{A}$,
> while "post-multiply (or right multiply) $\mathbf{A}$ by $\mathbf{C}$"
> means $\mathbf{A}.\mathbf{C}$

And because two sources are better than one, from
[this "ohio-state" course][ohio-matrices]:

> Pre-multiplication is to multiply the new matrix $\mathbf{B}$ to the left
> of the existing matrix $\mathbf{A}$ to get the result
> $\mathbf{C} = \mathbf{B}.\mathbf{A}$
>
> Post-multiplication is to multiply the new matrix $\mathbf{B}$ to the right
> of the existing matrix $\mathbf{A}$ to get the result
> $\mathbf{C} = \mathbf{A}.\mathbf{B}$

So it seems like Sun/Oracle got it right, and Google got it backward. Which
seems weird...   
I've filled a bug report on the
[Android Open Source Project Issue Tracker][bug-report] in order to know if
**I** missed something or if it's a real issue.

But it doesn't solve our problem: we have to be cautious when applying affine
transformations, because the order matters!

And because of the way we want to apply our transformations, in Android we're
going to make use of the `post` methods. But the `pre` methods are here also
and will simplify your like if you need this kind of operations.

### Demo

Again, performance aside, here’s a class that will rotate the Grumpy cat:

```java
package com.arnaudbos.android2d;
// imports stripped

public class MainActivity extends AppCompatActivity {
    // code stripped

    private enum MatrixConcatenation {
        PRE, POST
    }

    private static final float THETA = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display Grumpy cat
        Drawable d = getDrawable(R.drawable.grumpy);
        view = new ImageView(this);
        view.setImageDrawable(d);
        setContentView(view);

        // Center Grumpy cat
        view.setScaleType(ImageView.ScaleType.MATRIX);
        final float[] dimensions = getSize(this);
        width = dimensions[0];
        height = dimensions[1];
        matrix = center(width, height, d);
        view.setImageMatrix(matrix);
    }

    private static Matrix center(float width, float height, Drawable d) {
        final float drawableWidth = d.getIntrinsicWidth();
        final float drawableHeight = d.getIntrinsicHeight();
        final float widthScale = width / drawableWidth;
        final float heightScale = height / drawableHeight;
        final float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
        Matrix m = new Matrix();
        m.postScale(scale, scale);
        m.postTranslate((width - drawableWidth * scale) / 2F,
                (height - drawableHeight * scale) / 2F);
        return m;
    }

    private static void rotateGrumpyCat(ImageView view, float x, float y,
                                        Matrix matrix, MatrixConcatenation p) {
        switch (p) {
            case PRE:
                matrix.preTranslate(-x, -y);
                matrix.preRotate(THETA);
                matrix.preTranslate(x, y);
                break;
            case POST:
                matrix.postTranslate(-x, -y);
                matrix.postRotate(THETA);
                matrix.postTranslate(x, y);
                break;
        }
        view.setImageMatrix(matrix);
    }
}    
```

{{< gallery title="Android rotation demo of pre and post rotate" >}}
  {{% galleryimage file="/img/2d-transformations-android-java/grumpy-init.png"
  size="1080x1920" caption="Initial state: Matrix is scaled so that Grumpy is centered"
  width="260px" %}}
  {{% galleryimage file="/img/2d-transformations-android-java/grumpy-rotate.png"
  size="1080x1920" caption="Post-concatenating the transformation matrices"
  width="260px" %}}
  {{% galleryimage file="/img/2d-transformations-android-java/grumpy-out.png"
  size="1080x1920" caption="Pre-concatenating the transformation matrices"
  width="260px" %}}
{{< /gallery >}}

This time you can see `postScale` and `postTranslate` being called inside
`center` in order to scale the image and have the Grumpy cat centered inside
its view. This is just the initial phase.

The interesting part is the `rotateGrumpyCat` method, which is supposed to
rotate the Grumpy cat around a point, the center, but you see the different
results:

* `post` rotate gives the expected result, the Grumpy cat is rotate
  *"in place"* by 30 degrees
* `pre` rotate sends our little buddy out of the screen.

## Conclusion

Well, it's been fun writing those two articles. I definitely spent more time
writing the first one, which is full of math, than this one.  
I hope you now have a better understanding of how matrices work and how to
manipulate them in order to apply the transformations you want. I've
kept the code examples really simple on purpose.

If you have questions or feedback, please leave a comment below.

## Acknowledgements

I want to address my warmest thank you to the following people, who helped me
during the review process of this article, by providing helpful feedbacks and
advices:

* Igor Laborie ([@ilaborie][ilaborie])
* Hadrien Toma ([@HadrienToma][HadrienToma])

## Links

* Source code for [the Java example on Github][java-source]
* Source code for [the Android example on Github][android-source]

[matrices-for-developers]: ../matrices-for-developers
[jdk-affine-transform]: https://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html
[jdk-affine-transform-src]: http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/java/awt/geom/AffineTransform.java
[android-matrix]: https://developer.android.com/reference/android/graphics/Matrix.html
[android-matrix-src]: https://android.googlesource.com/platform/frameworks/base/+/master/graphics/java/android/graphics/Matrix.java
[android-matrix-h]: https://android.googlesource.com/platform/frameworks/base/+/master/core/jni/android/graphics/Matrix.h
[android-matrix-cpp]: https://android.googlesource.com/platform/frameworks/base/+/master/core/jni/android/graphics/Matrix.cpp
[skmatrix-h]: https://android.googlesource.com/platform/external/skia/+/master/include/core/SkMatrix.h
[skmatrix-cpp]: https://android.googlesource.com/platform/external/skia/+/master/src/core/SkMatrix.cpp
[wiki-matrix-multi]: https://en.wikipedia.org/wiki/Matrix_multiplication
[ohio-matrices]: http://web.cse.ohio-state.edu/~whmin/courses/cse5542-2013-spring/6-Transformation_II.pdf
[java-source]: https://github.com/arnaudbos/i-rant/tree/develop/static/code/2d-transformations-android-java/JavaAffineTransform
[android-source]: https://github.com/arnaudbos/i-rant/tree/develop/static/code/2d-transformations-android-java/AndroidMatrix
[bug-report]: https://code.google.com/p/android/issues/detail?id=229852&q=matrix&colspec=ID%20Status%20Priority%20Owner%20Summary%20Stars%20Reporter%20Opened
[ilaborie]: https://twitter.com/ilaborie
[HadrienToma]: https://twitter.com/HadrienToma
