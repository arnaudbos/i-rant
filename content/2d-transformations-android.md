---
title: 2D Transformations with Android
date: 2016-12-19T15:08:23+01:00
description:
tags: ["java","android","matrix"]
categories: ["java","android"]
draft: true
highlight: true
math: true
gallery: true
---


A few weeks ago I was on an `android-user-group` channel,
when someone posted a question about Android's
[Matrix.postScale(sx, sy, px, py)][Matrix.postScale] method and how it works
because it was *"hard to grasp"*.

Coincidence: a few months ago, at the beginning of 2016, I finished a freelance
project on an [Android application][climbing-away] where I had to implement
an exciting feature:

The user, after buying and downloading a digital topography of a crag, had
be able to view the crag which was composed of:

* a picture of the cliff,
* a SVG file containing the overlay of the climbing routes.

The user had to have the ability to pan and zoom at will and have the routes
layer "*follow*" the picture.

{{< gallery title="Android app screenshots" >}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-1.png"
  size="1080x1920" caption="Climbing away - App screenshot 1" %}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-2.png"
  size="1080x1920" caption="Climbing away - App screenshot 2" %}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-3.png"
  size="1080x1920" caption="Climbing away - App screenshot 3" %}}
{{< /gallery >}}

## Technical challenge

In order to have the overlay of routes follow the user's actions, I found I
had to get my hands dirty by overloading an Android `ImageView`, draw onto the
`Canvas` and deal with finger gestures.  
As a good engineer: I searched on Stack Overflow 😅  
And I discovered I'd need the `android.graphics.Matrix` class for 2D
transformations.

The problem with this class, is that it might seem obvious what it does, but if
you have no mathematical background, it's still quite mysterious.

> boolean postScale (float sx, float sy, float px, float py)
>
> Postconcats the matrix with the specified scale. M' = S(sx, sy, px, py) * M

Yeah, cool, so it *scales* something with some parameters and it does it with
some kind of multiplication. I don't get it:

* What does it do exactly? Scales a matrix? What's that supposed to mean, I
  want to scale the canvas...
* What should I use, `preScale` of `postScale`? Do I try both while I get the
  input parameters from my gesture detection code and enter an infite loop of
  trial and error guesstimates? (No. Way.)

So at this very moment of the development process I realized I needed to learn
basic math skills about [matrices][wiki-matrices] that I had forgotten many
years ago, after finishing my first two years of uni 😱

***WWW to the rescue!***

While searching around I've found a number of good resources and was able to
learn some math again, and it felt great. It also helped me solve my 2D
transformations problems by applying my understandings as code in Java and
Android.

So, given the discussion I've had on the channel I've mentioned above, it
seems I was not the only one struggling with matrices, trying to make sense of
it and using these skills with Android's Matrix class and methods,
so I thought I'd write an article.

## Table of contents

<div id="toc" class="well col-md-12">
  <!-- toc -->
</div>

## Affine transformations with Java

When I was working on the project I mentioned at the beginning of this article,
I was constantly moving back and forth between the
[JDK's implementation of affine transformations][jdk-affine-transform] and the
[Android SDK's implementation of matrices][android-matrix].

I find the `java.awt.geom.AffineTransform` class fairly well featured
but not that much, plus it is a bit ambiguous. Fortunately, the documentation is
good, it's not perfect but at least it's better than Android's one on this
topic as we shall see later.

The Javadoc starts with a reminder of what are 2D affine transformations and a
matrix multiplication pattern to transform coordinates.

<div>
$$
\begin{bmatrix}
x'\\
y'\\
1
\end{bmatrix}
=
\begin{bmatrix}
m00 & m01 & m02\\
m10 & m11 & m12\\
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
m00 . x + m01 . y + m02\\
m10 . x + m11 . y + m12\\
1
\end{bmatrix}
$$
</div>

That's neat, you have to appreciate the effort there (I mean, is your
Javadoc that great? 😇 ), and Android's Javadoc doesn't have it so...

The way this pattern is written lets us see a glimpse of implementation details,
right? Those `m00`, `m01` and etc, they (not so) strangely resemble stringified
versions of indexes in a two-dimensional array.

### Ambiguous — Part 1

So what is *"ambiguous"* with this class? Granted it might be a matter of taste,
but if you look at the constructor
`AffineTransform(m00, m10, m01, m11, m02, m12)` and the method
`setTransform(m00, m10, m01, m11, m02, m12)`, they only take 6 input
parameters.

While it makes perfectly sense to not take as inputs parameters that are fixed
(`0, 0, 1`) in the context of 2D affine transformations, I find it disturbing.

More disturbing perhaps, is the ordering of those parameters.

If you make the parallel between those and our `$a$`, `$b$`, etc. to the `$i$`
values we saw in the matrices above, you notice that the reading direction is not the
same.  
With `$a$`, `$b$`, etc., we used to read more *"naturally"* I would say, like
normal english written text: line by line.  
Whereas `$m00$`, `$m10$`, etc. is reading the matrix column by column.

I'm not saying one is better than the other, just that I'm more familiar with
the first one, and that it's worth pointing at it to clarify the use of this
class. Because the `getMatrix(flatmatrix)` method will fill in an
array containing the entries of the matrix in that specific order.  
Also, `getMatrix` *"Retrieves the 6 specifiable values in the 3x3 affine
transformation matrix"*, which means it will only give you those `$m00$`,
`$m10$`, etc., entries, not the ones from the third row.

### Well featured but not that much

To understand what I mean, let's try to execute the kind of transformations we
have seen throughout the first part of this article.

##### 1. Can we translate?

Yes! We have `translate(tx, ty)`:

> Concatenates this transform with a translation transformation.

We'll see what "concatenates" means in this context in a moment, for now what
we understand is that we have a method to apply a translation transformation.

##### 2. Can we shear?

Yes, but only by constants, not by angles, we have `shear(shx, shy)`:

> Concatenates this transform with a shearing transformation.

##### 3. Can we scale?

Yes! We have `scale(sx, sy)`:

> Concatenates this transform with a scaling transformation.

##### 4. Can we reflect?

Not directly, at least I don't see anything doing a reflexion directly, so we
either have to scale by negative values, or to use
`setTransform(-1, 0, 0, -1, 0, 0)` (for example) manually and then
`concatenate`.

##### 5. Can we rotate?

Yes! We have `rotate(theta)`:

> Concatenates this transform with a rotation transformation.

Beware: `theta` here is in radians, not in degrees.

##### 6. Can we scale on an anchor point?

No. You will have to compose your transformation as we've done it "by hand"
above, with a combination of `scale(sx, sy)` and `translate(tx, ty)`.

##### 7. Can we rotate around an anchor point?

Yes! We have `rotate(theta, anchorx, anchory)`:

> Concatenates this transform with a transform that rotates coordinates around
an anchor point.

##### 8. Can we transform points?

Yes! We have several methods available in order to transform points (even
shapes) from their original position to their new coordinates after the
transformation has been applied.

##### Why am I not happy with this?

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

All the transformations we've seen in the first part of this article are
defined this way:  

<div>
$$
P' = \mathbf{T}.P
$$
</div>

Where:

* `$P$` is a point
* `$P'$` is the point where `$P$` will land after the transformation has been applied
* `$\mathbf{T}$` is a transformation matrix
* `$\mathbf{T}$` is the product of many transformations matrices, applied
  in the reverse order: that is for transforming by `$\mathbf{A}$`, then
  `$\mathbf{B}$`, then `$\mathbf{C}$` we have
  `$\mathbf{T} = \mathbf{C} . \mathbf{B} . \mathbf{A} $`, and reciprocally.

Now, look at the definition of the description of the
`concatenate(AffineTransform Tx)` method:

> Concatenates an AffineTransform Tx to this AffineTransform Cx in the most commonly useful way to provide a new user space that is mapped to the former user space by Tx. Cx is updated to perform the combined transformation. Transforming a point p by the updated transform Cx' is equivalent to first transforming p by Tx and then transforming the result by the original transform Cx like this: Cx'(p) = Cx(Tx(p)) In matrix notation, if this transform Cx is represented by the matrix __[this__] and Tx is represented by the matrix __[Tx__] then this method does the following:
>
> __[this__] = __[this__] x __[Tx__]

In our notation this gives that for transforming by `$\mathbf{A}$`, then
`$\mathbf{B}$`, then `$\mathbf{C}$` we have:

<div>
$$
\mathbf{this}
=
\left( \left( \mathbf{this} . \mathbf{A} \right) . \mathbf{B} \right) . \mathbf{C}\\
\Leftrightarrow
\mathbf{this} = \mathbf{this} . \mathbf{Tx}
\text{ where }
\mathbf{Tx} = \mathbf{A} . \mathbf{B} . \mathbf{C}
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

As we've seen, matrix multiplication is non-commutative, so this will lead to
very different results than what you might expect!

The good news: there's a method `preConcatenate(AffineTransform Tx)` that does
what we want:

> __[this__] = __[Tx__] x __[this__]

The bad news: you won't be able to represent your transformations with the
built-in `translate`, `scale`, `rotate` as is. Because they don't behave the
way you think.  
At least they don't behave the way ***I*** think about transformations, which is
the one I've described in the matrices section above.

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

For instance, the 2x zoom at the center of the rectangle from our previous
example can be achieved in the following way:

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

{{< img src="/img/2d-transformations-android/java-zoom-at-center.png"
title="Demo of Java 2D zooming: in black the original square, in red the unexpected transformation and in green the desired transformation"
alt="Demo of Java 2D zooming" width="100%">}}

## Affine transformations with Android

`TODO: Insert links and snippets of code showing research: android native source code + c++ matrix library`

Unlike Oracle, Google's `android.graphics.Matrix` class assumes you already
know your way around matrices. There's no reminders, no details about matrices,
no explanations.  
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

##### 1. Can we translate?

Yes! We have `preTranslate(dx, dy)` and `postTranslate(dx, dy)`:

> Pre/Post-concats the matrix with the specified translation.

##### 2. Can we shear?

Yes, but only by constants not by angles, and it's named "skew".  
We have `preSkew(kx, ky)` and  `postSkew(kx, ky)`:

> Pre/Post-concats the matrix with the specified skew.

We also have `preSkew(kx, ky, px, py)` and `postSkew(kx, ky, px, py)` in order
to skew not around the origin, by around a given anchor point. That's nice.

##### 3. Can we scale?

Yes! We have `preScale(sx, sy)` and `postScale(sx, sy)`:

> Pre/Post-concats the matrix with the specified scale.

##### 4. Can we reflect?

Again, not directly, we can scale by negative values, or we can use
`setValues({-1, 0, 0, 0, -1, 0, 0, 0, 1})` (for example) and then
`postConcat`.

##### 5. Can we rotate?

Yes! We have `preRotate(degrees)` and `postRotate(degrees)`:

> Pre/Post-concats the matrix with the specified rotation.

##### 6. Can we scale on an anchor point?

Yes! We have `preScale(sx, sy, px, py)` and `postScale(sx, sy, px, py)`:

> Pre/Post-concats the matrix with the specified scale.

##### 7. Can we rotate around an anchor point?

Yes! We have `preRotate(degrees, px, py)` and `postRotate(degrees, px, py)`:

> Pre-Post-concats the matrix with the specified rotation.

##### 8. Can we transform points?

Also yes! We have several methods available in order to transform points and
shapes from their original position to their new coordinates after the
transformation has been applied.

### Ambiguous

Yes, I like this word...

The API is undeniably well featured, provides `pre` and `post` methods for
the most common transformations, a `setValues` method to create matrices of
any shape, and also `preConcat(Matrix other)` and `postConcat(Matrix other)`.

What do they do?

##### preConcat

> Preconcats the matrix with the specified matrix. M' = M * other

So, if I read correctly, this is equivalent to:


<div>
$$
\mathbf{this} = \mathbf{this} . \mathbf{Tx}
$$
</div>

Wait... in Java's `AffineTransform`, this was the equivalent of the
`concatenate` method...

##### postConcat

> Postconcats the matrix with the specified matrix. M' = other * M

Again, if I read correctly, this is equivalent to:


<div>
$$
\mathbf{this} = \mathbf{Tx} . \mathbf{this}
$$
</div>

Wait... in Java's `AffineTransform`, this was the equivalent of the
`preConcatenate` method...

##### WTF dude?

Exactly. If you don't read the doc, **you're screwed** 💩

So who's right?

I've searched a few minutes on the Interwebs and here's what I've found
[from Wikipedia][wiki-matrix-multi]:

> "pre-multiply (or left multiply) `$\mathbf{A}$` by `$\mathbf{B}$`" means
> `$\mathbf{B}.\mathbf{A}$`,
> while "post-multiply (or right multiply) `$\mathbf{A}$` by `$\mathbf{C}$`"
> means `$\mathbf{A}.\mathbf{C}$`

And because two sources are better than one, from
[this "ohio-state" course][ohio-matrices]:

> Pre-multiplication is to multiply the new matrix `$\mathbf{B}$` to the left
> of the existing matrix `$\mathbf{A}$` to get the result
> `$\mathbf{C} = \mathbf{B}.\mathbf{A}$`
>
> Post-multiplication is to multiply the new matrix `$\mathbf{B}$` to the right
> of the existing matrix `$\mathbf{A}$` to get the result
> `$\mathbf{C} = \mathbf{A}.\mathbf{B}$`

So it seems like Sun/Oracle got it right, and Google got it backward. But it
doesn't solve our problem: we have to be cautious when applying affine
transformations, because the order matters!

And because of the way we want to apply our transformations, in Android we're
going to make use of the `post` methods. But the `pre` methods are here also
and will simplify your like if you need this kind of operations.

## Acknowledgements

I want to address my warmest thank you to the following people, who helped me
during the review process of this article, by providing helpful feedbacks and
advices:

* Igor Laborie ([@ilaborie][ilaborie])
* Hadrien Toma

## Links

Source code for the Java 2D example: [on Github][java-2d-source]

`TODO` Insert moar! Alot moar!

[Matrix.postScale]: https://developer.android.com/reference/android/graphics/Matrix.html#postScale(float,%20float,%20float,%20float)
[climbing-away]: https://play.google.com/store/apps/details?id=fr.climbingaway
[wiki-matrices]: https://en.wikipedia.org/wiki/Matrix_(mathematics)
[wiki-elim-variable]: https://en.wikipedia.org/wiki/System_of_linear_equations#Elimination_of_variables
[wiki-row-reduction]: https://en.wikipedia.org/wiki/System_of_linear_equations#Row_reduction
[khan-alg-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices
[khan-intro-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-intro-to-matrices/a/intro-to-matrices
[khan-addsub-matrices]: https://www.khanacademy.org/bigbingo_redirect?continue=https%3A%2F%2Fwww.khanacademy.org%2Fmath%2Falgebra-home%2Falg-matrices%2Falg-adding-and-subtracting-matrices%2Fv%2Fmatrix-addition-and-subtraction-1&conversion_ids=condensed_tutorial_title_click
[wiki-scalar]: https://en.wikipedia.org/wiki/Scalar_(mathematics)
[khan-mult-scalar]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-multiplying-matrices-by-scalars/v/scalar-multiplication
[khan-mult-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-multiplying-matrices-by-matrices/a/multiplying-matrices
[wiki-apples-oranges]: https://en.wikipedia.org/wiki/Apples_and_oranges
[khan-defined-vid]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/v/defined-and-undefined-matrix-operations
[khan-defined-course]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/matrix-multiplication-dimensions
[khan-transf-vector]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/v/transforming-position-vector
[khan-transf-polygone]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/v/matrix-transformation-triangle
[khan-matrices-transform]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/a/matrices-as-transformations
[khan-identity-matrix]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/intro-to-identity-matrices
[khan-mult-properties]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/properties-of-matrix-multiplication
[matrices-texas]: https://www.cs.utexas.edu/~fussell/courses/cs384g/lectures/lecture07-Affine.pdf
[trigonometry]: http://www.mathsisfun.com/algebra/trigonometry.html
[datagenetics-rotation]: http://datagenetics.com/blog/august32013/
[matrix-rotation-video]: https://www.youtube.com/watch?v=h11ljFJeaLo
[homogeneous-coordinates]: https://en.wikipedia.org/wiki/Homogeneous_coordinates
[jdk-affine-transform]: https://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html
[android-matrix]: https://developer.android.com/reference/android/graphics/Matrix.html
[wiki-matrix-multi]: https://en.wikipedia.org/wiki/Matrix_multiplication
[ohio-matrices]: http://web.cse.ohio-state.edu/~whmin/courses/cse5542-2013-spring/6-Transformation_II.pdf
[java-2d-source]: https://github.com/arnaudbos/i-rant/tree/develop/static/code/matrices-for-developers/JavaAffineTransform
[ilaborie]: https://twitter.com/ilaborie
