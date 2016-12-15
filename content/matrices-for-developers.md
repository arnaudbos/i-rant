---
title: Matrices for developers
date: 2016-11-19T15:08:29+01:00
description:
tags: ["java","android","matrix"]
categories: ["java","android"]
draft: true
highlight: true
math: true
klipse: true
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

## Technical challenge

In order to have the overlay of routes follow the user's actions, I had to
apply 2D transformations. As a good engineer I searched on Stack Overflow
how to do that and found I had to get my hands dirty by overloading an Android
`ImageView`, draw onto the `Canvas`, deal with finger gestures and use the
`android.graphics.Matrix` class for 2D transformations.

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

{{< toc >}}

## What is a matrix?

The first resource you might encounter when trying to understand 2D
transformations are articles about *"Transformation matrix"* and
*"Affine transformations"* on Wikipedia:

* https://en.wikipedia.org/wiki/Transformation_matrix
* https://en.wikipedia.org/wiki/Transformation_matrix#Affine_transformations
* https://en.wikipedia.org/wiki/Affine_transformation

I don't know you, but with this material, I almost got everything — wait...

{{< img src="/img/matrices-for-developers/awkward-seal.png"
alt="Awkward seal" width="100%">}}

**NOPE! Nevermind, I didn't get anything at all.**

Luckily, on ***Khan Academy*** you will find a very well taught
[algebra course about matrices][khan-alg-matrices].

If you have this kind of problem, I encourage you to take the time needed to
follow this course until you reach that "AHA" moment. It's just a few hours of
investment (it's free) and you won't regret it.

Why? Because [matrices][wiki-matrices] are good at representing data, and
operations on matrices can help you solve problems on this data. For instance,
remember having to solve systems of linear equations at school?  
The most common ways (at least the two **I**'ve studied) to solve a system
like that is with the [elimination of variables][wiki-elim-variable] method
or the [row reduction][wiki-row-reduction] method. But you can also use
matrices for that, which leads to interesting algorithms.  
Matrices are used heavily in every branch of science, and they can also be
used for linear transformation to describe the position of points in space,
and this is the use case we will study in this article.

### Anatomy

Simply put, a [matrix is a *2D array*][khan-intro-matrices]. In fact, talking
about a `$m×n$` matrix relates to an array of length `$m$` in which
each item is also an array but this time of length `$n$`. Usually, `$m$`
represents a rows' number and `$n$` a columns' number. Each element in the
matrix is called an *entry*.  
A matrix is represented by a bold capital letter,
and each entry is represented by the same letter, but in lowercase and suffixed
with its row number and column number, in this order. For example:

<div>
$$
\mathbf{A} =
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{bmatrix}
$$
</div>


Now what can we do with it? We can define an algebra for instance: like
[addition, subtraction][khan-addsub-matrices] and
[multiplication][khan-mult-matrices] operations, for fun and profit. 🤓

### Addition/Subtraction

[Addition and subtraction of matrices][khan-addsub-matrices] is done by adding
or subtracting the corresponding entries of the operand matrices. Like so:

<div>
$$
\mathbf{A} + \mathbf{B} =
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{bmatrix}
+
\begin{bmatrix}
b_{11} & b_{12} & \cdots & b_{1n}\\
b_{21} & b_{22} & \vdots & b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
b_{m1} & b_{m2} & \cdots & b_{mn}
\end{bmatrix}
=
\begin{bmatrix}
a_{11}+b_{11} & a_{12}+b_{12} & \cdots & a_{1n}+b_{1n}\\
a_{21}+b_{21} & a_{22}+b_{22} & \vdots & a_{2n}+b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1}+b_{m1} & a_{m2}+b_{m2} & \cdots & a_{mn}+b_{mn}
\end{bmatrix}
$$
</div>

<div>
$$
\mathbf{A} - \mathbf{B} =
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{bmatrix}
-
\begin{bmatrix}
b_{11} & b_{12} & \cdots & b_{1n}\\
b_{21} & b_{22} & \vdots & b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
b_{m1} & b_{m2} & \cdots & b_{mn}
\end{bmatrix}
=
\begin{bmatrix}
a_{11}-b_{11} & a_{12}-b_{12} & \cdots & a_{1n}-b_{1n}\\
a_{21}-b_{21} & a_{22}-b_{22} & \vdots & a_{2n}-b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1}-b_{m1} & a_{m2}-b_{m2} & \cdots & a_{mn}-b_{mn}
\end{bmatrix}
$$
</div>

Corollary to this definition we can deduce that in order to be *defined*, a matrix
addition or subtracting must be performed against two matrices of same
dimensions `$m×n$`, otherwise the *"corresponding entries"* bit would
have no sense:  
Grab a pen and paper and try to add a `$3×2$` matrix to a `$2×3$`
matrix. What will you do with the last *row* of the first matrix? Same question
with the last *column* of the second matrix?  
If you don't know, then you've reach the same conclusion as the mathematicians
that defined matrices additions and subtraction, pretty much 😋

#### Examples

<section class="split-half">
<div class="left">
$$
\begin{align}
\text{Addition}\\
\mathbf{A} + \mathbf{B}
&=
\begin{bmatrix}
4 & -8 & 7\\
0 & 2 & -1\\
15 & 4 & 9
\end{bmatrix}
+
\begin{bmatrix}
-5 & 2 & 3\\
4 & -1 & 6\\
0 & 12 & 3
\end{bmatrix}\\
&=
\begin{bmatrix}
4+\left(-5\right) & \left(-8\right)+2 & 7+3\\
0+4               & 2+\left(-1\right) & \left(-1\right)+6\\
15+0              & 4+12              & 9+3
\end{bmatrix}\\
\mathbf{A} + \mathbf{B}
&=
\begin{bmatrix}
-1 & -6 & 10\\
4  & 1  & 5\\
15 & 16 & 12
\end{bmatrix}
\end{align}
$$
</div>
<div class="right">
$$
\begin{align}
\text{Subtraction}\\
\mathbf{A} - \mathbf{B}
&=
\begin{bmatrix}
4  & -8 & 7\\
0  & 2  & -1\\
15 & 4  & 9
\end{bmatrix}
-
\begin{bmatrix}
-5 & 2 & 3\\
4 & -1 & 6\\
0 & 12 & 3
\end{bmatrix}\\
&=
\begin{bmatrix}
4-\left(-5\right) & \left(-8\right)-2 & 7-3\\
0-4               & 2-\left(-1\right) & \left(-1\right)-6\\
15-0              & 4-12              & 9-3
\end{bmatrix}\\
\mathbf{A} + \mathbf{B}
&=
\begin{bmatrix}
9  & -10 & 4\\
-4 & 3   & -7\\
15 & -8  & 6
\end{bmatrix}
\end{align}
$$
</div>
</section>

<br>

### Matrix multiplication

`TODO` Remove this:

{{< klipse >}}

Throughout all my math schooling I was said something along the lines of
*"you can only perform operations against the same types of values"*,
illustrated by sentences like
*"[you can't add apples to oranges][wiki-apples-oranges], it makes no
sense"*, in order to express the importance of units.  
Well it turns out that multiplying apples and oranges *is* allowed. And it
can be applied to matrices: we can multiply matrices by numbers, and we can
multiply matrices by matrices.

In the first case though, the number is not just a number (semantically).
You don't multiply a matrix by a number, you multiply a matrix by a
[**scalar**][wiki-scalar]. In order to
[multiply a matrix by a scalar][khan-mult-scalar], we have to multiply each
entry in the matrix by the scalar, which will give us another matrix as a
result.

<div>
$$
k . \mathbf{A}
=
k .
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{bmatrix}
=
\begin{bmatrix}
k.a_{11} & k.a_{12} & \cdots & k.a_{1n}\\
k.a_{21} & k.a_{22} & \vdots & k.a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
k.a_{m1} & k.a_{m2} & \cdots & k.a_{mn}
\end{bmatrix}
$$
</div>

And a little example:

<div>
$$
4 .
\begin{bmatrix}
0  & 3  & 12\\
7  & -5 & 1\\
-8 & 2  & 0
\end{bmatrix}
=
\begin{bmatrix}
0   & 12  & 48\\
28  & -20 & 4\\
-32 & 8   & 0
\end{bmatrix}
$$
</div>

The second type of multiplication operation is the
[multiplication of matrices by matrices][khan-mult-matrices]. This operation
is a little bit more complicated than addition/subtraction because in order
to multiply a matrix by a matrix we don't simply multiply the corresponding
entries. I'll just quote wikipedia on that one:

> if `$A$` is an `$m×n$` matrix and `$B$` is an `$n×p$` matrix, their matrix
> product `$AB$` is an `$m×p$` matrix, in which the `$n$` entries across a
> row of `$A$` are multiplied with the `$n$` entries down a columns of `$B$`
> and summed to produce an entry of `$AB$`

This hurts my brain, let's break it down.

See this simple `$2×2$` matrix
`$A = \begin{bmatrix}a_{11} & a_{12}\\a_{21} & a_{22}\end{bmatrix}$`
and this `$2×2$` matrix
`$B = \begin{bmatrix}b_{11} & b_{12}\\b_{21} & b_{22}\end{bmatrix}$`.  
We have `$m=2$`, `$n=2$` and `$p=2$` so the multiplication will give
`$AB = \begin{bmatrix}ab_{11} & ab_{12}\\ab_{21} & ab_{22}\end{bmatrix}$`.

* *"the `$n$` entries across a row of `$A$`"* means is that each row in
  `$A$` is an array of `$n=2$` entries: if we take the first row we get
  `$a_{11}$` and `$a_{12}$`
* *"the `$n=2$` entries down a columns of `$B$`"* means that each column of
  `$B$` is also an array of `$n=2$` entries: in the first column we get
  `$b_{11}$` and `$b_{21}$`.  
* *"are multiplied with"* means that each entry in `$\mathbf{A}$`'s row
  must be multiplied with it corresponding (first with first, second with
  second, etc.) entry in `$\mathbf{B}$`'s column: `$a_{11}×b_{11}$` and
  `$a_{12}×b_{21}$`
* *"And summed to produce an entry of `$AB$`"* means that we must add the
  products of these corresponding rows and columns entries in order to get
  the entry of the new matrix at this row number and column number: in our case
  we took the products of the entries in the first row in the first matrix with
  the entries in the first column in the second matrix, so this will give us the
  entry in the first row and first column of the new matrix:
  `$a_{11}×b_{11} + a_{12}×b_{21}$`

To plagiate wikipedia, here is the formula:

<div>
$$
\mathbf{A} =
\begin{bmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{bmatrix}
\text{, }
\mathbf{B} =
\begin{bmatrix}
b_{11} & b_{12} & \cdots & b_{1p}\\
b_{21} & b_{22} & \vdots & b_{2p}\\
\vdots & \vdots & \ddots & \vdots\\
b_{n1} & b_{n2} & \cdots & b_{np}
\end{bmatrix}\\
\mathbf{AB} =
\begin{bmatrix}
ab_{11} & ab_{12} & \cdots & ab_{1p}\\
ab_{21} & ab_{22} & \vdots & ab_{2p}\\
\vdots & \vdots & \ddots & \vdots\\
ab_{m1} & ab_{m2} & \cdots & ab_{mp}
\end{bmatrix}\\
\text{where }
ab_{ij}=\sum_{k=1}^{m}a_{ik}b_{kj}
$$
</div>

Ok I realize I don't have any better way to explain this so here is a visual
representation of the matrix multiplication process and an example:

<div style="text-align: center;">
<a title="By File:Matrix multiplication diagram.svg:User:Bilou See below. [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons" href="https://commons.wikimedia.org/wiki/File%3AMatrix_multiplication_diagram_2.svg"><img width="256" alt="Matrix multiplication diagram 2" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Matrix_multiplication_diagram_2.svg/256px-Matrix_multiplication_diagram_2.svg.png"/></a>
</div>

<div>
$$
\mathbf{A} =
\begin{bmatrix}
4  & 3\\
0  & -5\\
2  & 1\\
-6 & 8
\end{bmatrix}
\text{, }
\mathbf{B} =
\begin{bmatrix}
7  & 1 & 3\\
-2 & 4 & 1
\end{bmatrix}\\
\begin{align}
\mathbf{AB}
&=
\begin{bmatrix}
4×7+3×\left(-2\right)               & 4×1+3×4               & 4×3+3×1\\
0×7+\left(-5\right)×\left(-2\right) & 0×1+\left(-5\right)×4 & 0×3+\left(-5\right)×1\\
2×7+1×\left(-2\right)               & 2×1+1×4               & 2×3+1×1\\
\left(-6\right)×7+8×\left(-2\right) & \left(-6\right)×1+8×4 & \left(-6\right)×3+8×1
\end{bmatrix}\\
\mathbf{AB}
&=
\begin{bmatrix}
28-6   & 4+12  & 12+3\\
0+10   & 0-20  & 0-5\\
14-2   & 2+4   & 6+1\\
-42-16 & -6+32 & -18+8
\end{bmatrix}\\
\mathbf{AB}
&=
\begin{bmatrix}
22  & 16  & 15\\
10  & -20 & -5\\
12  & 6   & 7\\
-58 & 26  & -10
\end{bmatrix}
\end{align}
$$
</div>

Remember:

> In order for matrix multiplication to be defined, the number of columns in
> the first matrix must be equal to the number of rows in the second matrix.

Otherwise you just **can't** multiply, period.

More details [here][khan-defined-vid] and [here][khan-defined-course] if you
are interested.

## Transformation matrices

Now that we know what is a matrix and how we can multiply matrices, we can see
why it is interesting for **2D transformations**.

As I've said previously, matrices can be used to represent systems of
linear equations. Suppose I give you this system:

<div>
$$
2x+y=5\\
-x+2y=0
$$
</div>

Now that you are familiar with matrix multiplications, maybe you can see this
coming, but we can definitely express this system of equations as the following
matrix multiplication:

<div>
$$
\begin{bmatrix}
2  & 1\\
-1 & 2
\end{bmatrix}
.
\begin{bmatrix}
x\\y
\end{bmatrix}
=
\begin{bmatrix}
5\\0
\end{bmatrix}
$$
</div>

If we go a little farther, we can see something else based on the matrices
`$\begin{bmatrix}x\\y\end{bmatrix}$` and
`$\begin{bmatrix}5\\0\end{bmatrix}$`.  
We can see that they can be used to reprensent ***points*** in the Cartesian
plane, right? A point can be represented by a vector originating from origin,
and a vector is just a `$2×1$` matrix.

What we have here, is a matrix multiplication that represents the
transformation of a point 

The next video will show you how you can use a matrix to **transform a point**!

{{< youtube Kh8HKAxdEyw >}}

<br>

Or **a series of points**!

{{< youtube RKBSX-6pKgY >}}

<br>

Notice the nature of the transformation is not clear yet, the transformation
matrix was picked at random, and yet we see how interesting and useful it is
for 2D manipulation of graphics.

For now, I suppose all you know is the type of transformations you want to
apply: `rotation`, `scale` or `translation` and the parameters.

So how do you go from `scale by a factor of 2` and `rotate 90 degrees clockwise`
to a transformation matrix?

Well the answer is:

{{< img src="/img/matrices-for-developers/moar-math-stuff.jpg"
alt="Moar math stuff with smiling cat meme" width="60%">}}

### More math stuff

More particularly you'll need to read [this course on *Matrices as
transformations* (which is full of fancy plots and
animations)][khan-matrices-transform] and particularly its last part:
***Representing two dimensional linear transforms with matrices***.

Come back here once you've read it, or it's gonna hurt 😅

---

Ok I suppose you've read the course I linked to above, so in this course we've
learned that:

* a position vector `$ \begin{bmatrix} x\\  y \end{bmatrix} $` can be broken down as `$ \begin{bmatrix} x\\ y \end{bmatrix} = x \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix} + y \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix} $`.

* we've also learned that
`$ \begin{bmatrix} \color{Green} a\\ \color{Green} c \end{bmatrix} $` and `$ \begin{bmatrix} \color{Red} b\\ \color{Red} d \end{bmatrix} $` are the position vectors where `$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 1 \end{bmatrix} $` and `$ \begin{bmatrix} \color{Red} 1\\ \color{Red} 0 \end{bmatrix} $` will land respectively after the matrix `$ \mathbf{A} = \begin{bmatrix} \color{Green} a & \color{Red} b\\ \color{Green} c & \color{Red} d \end{bmatrix} $` has been applied.

* and that given the same transformation, `$ \begin{bmatrix} x\\ y \end{bmatrix} $` will land on `$ \begin{bmatrix} \color{Green} a x + \color{Red} b x\\ \color{Green} c x + \color{Red} d y \end{bmatrix} $`.

If you don't understand this conclusion, read again, take your time.

----

Now remember, our goal is to determine what `$ \mathbf{A} $` is, because we
know the transformation we want to apply but we're searching for the matrix we
should apply to our position vector(s) in order to transform our graphics.

Let's take the example of the series of points transformation video above
and turn the problem backward: we know where the position vectors will land,
but we're looking for `$ \mathbf{A} $`.

`TODO` Insert 2D plane with `$P_{(2,1)}$`, `$Q_{(-2,0)}$`, `$P'_{(5, 0)}$`
and `$Q'_{(-4, 2)}$`.

We know that:

<section class="split-half">
    <div class="left">$$ \begin{bmatrix} 2\\ 1 \end{bmatrix} \text{ lands on } \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="right">$$ \begin{bmatrix} -2\\ 0 \end{bmatrix} \text{ lands on } \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</section>

<br>

Which means:

<section class="split-half">
    <div class="left">$$ \begin{bmatrix} x\\ y \end{bmatrix} = \begin{bmatrix} 2\\ 1 \end{bmatrix} \text{ lands on } \begin{bmatrix} a.x+b.y\\ c.x+d.y \end{bmatrix} = \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="right">$$ \begin{bmatrix} x\\ y \end{bmatrix} = \begin{bmatrix} -2\\ 0 \end{bmatrix} \text{ lands on } \begin{bmatrix} a.x+b.y\\ c.x+d.y \end{bmatrix} = \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</section>

<br>

From which we can deduce:

<section class="split-half">
    <div class="left">$$ \begin{bmatrix} 2.a+1.b\\ 2.c+1.d \end{bmatrix} = \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="right">$$ \begin{bmatrix} -2.a+0.b\\ -2.c+0.d \end{bmatrix} = \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</section>

<br>

The right side gives us `$ a=2 $` and `$ c = -1 $`, with which we can deduce
`$ b=1 $` and `$ d=2 $` from the left side.

**And this, is our transformation matrix**:

<div>$$ \mathbf{A} = \begin{bmatrix} \color{Green} 2 & \color{Red} 1\\ \color{Green} -\color{Green} 1 & \color{Red} 2 \end{bmatrix} $$</div>

### The identity matrix

We don't know how to define a transformation matrix yet, but we know its form.  
So what do we do next? Remember the last section where we've seen that
a position vector `$ \begin{bmatrix} x\\  y \end{bmatrix} $` can be
broken down as `$
\begin{bmatrix}
x\\
y
\end{bmatrix}
= x \begin{bmatrix}
\color{Green} 1\\
\color{Green} 0
\end{bmatrix}
+ y \begin{bmatrix}
\color{Red} 0\\
\color{Red} 1
\end{bmatrix} $` ?

That's a pretty good starting point, we just laid out our "base" matrix:

<div>
$$
\begin{bmatrix}
\color{Green} 1 & \color{Red} 0\\
\color{Green} 0 & \color{Red} 1
\end{bmatrix}
$$
</div>

This matrix represents the base state of your plane, the matrix applied
to your plane when you have just loaded your image for example (granted
your image is the same size as its receiving container view).  
In other words, ***this is the matrix that, applied to a position vector will
return that same position vector***.

This matrix is called [the identity matrix][khan-identity-matrix]:

{{< youtube 3cnIa0fYJkY >}}

<br>

### Combining transformations

One more thing before we get concrete: *We want our user to be able
to combine/chain transformations* (like zooming and panning at the same time
for instance).

In order to chain multiple transformations we need to understand the
[properties of matrix multiplication][khan-mult-properties], and more
specifically the `non-commutative` and `associative` properties of matrix
multiplication.

Let's take yet another example:  
Imagine we want to apply transformations `$ \mathbf{B} $`, then transformation
`$ \mathbf{A} $` to our position vector `$ \vec{v} $`.

We have
`$
\vec{v'} = \mathbf{B} . \vec{v}
$`
and
`$
\vec{v''} = \mathbf{A} . \vec{v'}
$`,
which leads us to:

<div>
$$
\vec{v''} = \mathbf{A} . \left( \mathbf{B} . \vec{v} \right)
$$
</div>

From the previous link on matrix properties, we know that matrix multiplication
is `associative`, which gives us:


<div>
$$
\vec{v''} = \mathbf{A} . \left( \mathbf{B} . \vec{v} \right)
\Leftrightarrow
\vec{v''} = \left( \mathbf{A} . \mathbf{B} \right) . \vec{v}
$$
</div>

In conclusion, in order to apply multiple transformations at once, we can
multiply all our transformation matrices and apply the resulting transformation
matrix to our vector(s).

The previous link also taught us that matrix multiplication is **not**
commutative, so the order in which we multiply our transformation matrices
(`$ \mathbf{A} . \mathbf{B} $` or `$ \mathbf{B} . \mathbf{A} $`) will have
an impact on our final matrix and will lead to results, different
transformations.

## Types of transformations

There are several types of 2D transformations we are able to define using 2x2
dimensions matrices, and you've had a preview of most of them in this
course on [matrices as transformations][khan-matrices-transform].  
Namely:

* Scaling
* Reflexion
* Shearing
* Rotation

For the rest of this section imagine we have the point
`$ P_{\left(x, y\right)} $`, which represents any point of
an object on the plane, and we want to find the matrix to transform it into
`$ P'_{\left(x', y'\right)}$` such that

<div>
$$
\begin{bmatrix} x'\\y' \end{bmatrix} =
\mathbf{A} . \begin{bmatrix} x\\y \end{bmatrix} =
\begin{bmatrix} a & b\\c & d \end{bmatrix}
.
\begin{bmatrix} x\\y \end{bmatrix}
$$
</div>

### Scaling

Scaling (like zooming in by a factor of 2 for instance) might seem
straightforward to represent at the time of pinch-zoom/digital devices, right?
*"Simply multiply the coordinates by the scaling factor and you're done."*  
But the pitfall here is that you *might* want to have different horizontal and
vertical scaling factors for your transformation, I mean it's possible!

So we must differentiate between `$ s_{x} $` and `$ s_{y} $` which represent
the horizontal and vertical scaling factors, respectively.

The two equations this gives us are:

<div>
$$
x' = s_{x} . x \\
y' = s_{y} . y
$$
</div>

Knowing that:

<div>
$$
\begin{bmatrix} x'\\y' \end{bmatrix} =
\begin{bmatrix} a & b\\c & d \end{bmatrix}
.
\begin{bmatrix} x'\\y' \end{bmatrix}
$$
</div>

We can find `$a$`, `$b$`, `$c$` and `$d$`:

<section class="split-half">
    <div class="left">
$$
s_{x} . x = a . x + b . y\\
\Rightarrow
a = s_{x} \text{ and } b = 0
$$
</div>
    <div class="right">
$$
s_{y} . y = c . x + d . y\\
\Rightarrow
c = s_{y} \text{ and } d = 0
$$
</div>
</section>

<br>

In conclusion, the 2x2 scaling matrix for the factors
`$ \left(s_{x}, s_{y}\right) $` is

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix} s_{x} & 0\\0 & s_{y} \end{bmatrix}
$$
</div>

Which makes sense, right? I mean, scaling by a factor of `$1$` both on the
`$x$` and `$y$` axises will give:

<div>
$$
\begin{bmatrix} s_{x} & 0\\0 & s_{y} \end{bmatrix}
=
\begin{bmatrix} 1 & 0\\0 & 1 \end{bmatrix}
$$
</div>

Which is... the `identity` matrix! So nothing moves, basically.

### Reflexion

There are 2 types of reflexions we can think about right ahead: reflexion around
an axis, or around a point.  
To keep things simple we'll focus on reflexions around the `$x$` and `$y$`
axises (reflexion around the origin is the equivalent of applying a reflexion on
the `$x$` and `$y$` axises successively).

Reflexion around the `$x$` axis gives us:

<section class="split-half">
    <div class="left">
$$
x' = x\\
\Rightarrow
x = a . x + b . y\\
\Rightarrow
a = 1 \text{ and } b = 0
$$
</div>
    <div class="right">
$$
y' = -y\\
\Rightarrow
-y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = -1
$$
</div>
</section>

<br>

Funny, reflecting around the `$x$` axis is the same transformation as scaling
`$x$` by a factor of `$-1$` and `$y$` by a factor of `$1$`:

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix}
1 & 0\\
0 & -1
\end{bmatrix}
$$
</div>

And reflexion around the `$y$` axis gives us:

<section class="split-half">
    <div class="left">
$$
x' = -x\\
\Rightarrow
-x = a . x + b . y\\
\Rightarrow
a = -1 \text{ and } b = 0
$$
</div>
    <div class="right">
$$
y' = y\\
\Rightarrow
y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = 1
$$
</div>
</section>

<br>

The transformation matrix to reflect around the `$y$` axis is:

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix}
-1 & 0\\
0 & 1
\end{bmatrix}
$$
</div>

### Shearing

Now it gets a little bit trickier.

In most examples I've found, shearing is explained by saying the coordinates
are changed by adding a constant that measures the degree of shearing.  
For instance, a shear along the `$x$` axis is often represented showing a
rectangle with a vertex at `$\left(0, 1\right)$` is transformed into a
parallelogram with a vertex at `$\left(1, 1\right)$`.

`TODO`: (insert image of a plane with a shearing of alpha)

In this article, I want to explain it using the shearing angle, the angle
through which the axis is sheared. Let's call it `$\alpha$` (alpha).

Remember your [trigonometry][trigonometry] class?

> In a right-angled triangle:
>
> * the hypotenuse is the longest side
> * the the opposite side is the one at the opposite of a given angle
> * the adjacent side is the next to a given angle

If we look at the plane above, we can see that the new abscissa `$x'$` is
equals to the addition of `$x$` plus the opposite side of the triangle formed
by the `$y$` axis, the sheared version of the `$y$` axis and the segment
between the top left vertex of the rectangle and the top left vertex of the
parallelogram.

From our trigonometry class, we know that:

<div>
$$
\cos \left( \alpha \right) = \frac{adjacent}{hypotenuse}\\
\sin \left( \alpha \right) = \frac{opposite}{hypotenuse}\\
\tan \left( \alpha \right) = \frac{opposite}{adjacent}
$$
</div>

We know `$\alpha$`, but we don't know the length of the hypotenuse, so we
can't use the cosine function.  
But we know the adjacent side's length: it's `$y$`, so we can use the tangent
function find the opposite side's length.

<section class="split-half">
    <div class="left">
$$
x' = x + y . \tan \left( -\alpha \right) \\
\Rightarrow
x + y . \tan \left( -\alpha \right) = a . x + b . y\\
\Rightarrow
a = 1 \text{ and } b = \tan \left( -\alpha \right)
$$
</div>
    <div class="right">
$$
y' = y\\
\Rightarrow
y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = 1
$$
</div>
</section>

<br>

The transformation matrix to shear along the `$x$` direction is:

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix}
1 & \tan \left( -\alpha \right)\\
0 & 1
\end{bmatrix}
=
\begin{bmatrix}
1 & k_{x}\\
0 & 1
\end{bmatrix}\\
\text{where } k_{x} \text{ is the shearing constant}
$$
</div>

Similarly, the transformation matrix to shear along the `$y$` direction is:

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix}
1 & 0\\
\tan \left( -\beta \right) & 1
\end{bmatrix}
=
\begin{bmatrix}
1 & 0\\
k_{y} & 1
\end{bmatrix}\\
\text{where } k_{y} \text{ is the shearing constant}
$$
</div>

### Rotation

Rotations are yet a little bit more complex.

Let's take a closer look at it with an example of rotating (around the origin)
from a angle `$ \theta $` (theta).

<a title="By Nick Berry" href="http://datagenetics.com/blog/august32013/"><img style="background-color: #333;"
src="/img/matrices-for-developers/g22.png"></a>

This image shows a point `$ P $` before and after a rotation: `$ P $`
in the starting and ending planes respectively.

Notice how the coordinates of `$ P $` in each plane are the same:
`$ P $` has the same set of coordinates `$ \left( x, y\right) $` in both
planes.  
But now `$ P $` has ***new coordinates*** `$ \left( x, y\right) $` ***in the
first plane***.

Let's rename the coordinates of `$ P $` in the second plane as
`$ \left( x', y'\right) $`.
At this point we know `$ x' $` and `$ y' $`, because they are the same as the
original `$ x $` and `$ y $`. But we don't know the new `$ x $` and `$ y $`.  
On the other hand, we can now define the ***relationship*** between the new
coordinates `$ \left(x, y\right) $` and the coordinates in the rotated plane
`$ \left(x', y'\right) $`, right?

This is where [trigonometry][trigonometry] helps again, along with
[this post][datagenetics-rotation] and [this video][matrix-rotation-video].

<a title="By Nick Berry" href="http://datagenetics.com/blog/august32013/"><img style="background-color: #333;"
src="/img/matrices-for-developers/g32.png"></a>

Here we see that `$ x' $` (the blue line) can be expressed as the addition of
the
<span class="green-text">adjacent side of the green triangle</span>
plus
<span class="red-text">the opposite side of the red triangle.</span>  
And `$ y' $` as the subtraction of
<span class="green-text">the opposite side of the green triangle</span>
from
<span class="red-text">the adjacent side of the red triangle</span>.

<div>
$$
\cos \left( \theta \right) = \frac{adjacent}{hypotenuse} \Rightarrow adjacent = hypotenuse . \cos \left( \theta \right)\\
\sin \left( \theta \right) = \frac{opposite}{hypotenuse} \Rightarrow opposite = hypotenuse . \sin \left( \theta \right)
$$
</div>

So we can express our relationship as follows:

<div>
$$
\begin{align}
x' & = \color{Green}a\color{Green}d\color{Green}j\color{Green}a\color{Green}c\color{Green}e\color{Green}n\color{Green}t + \color{Red}o\color{Red}p\color{Red}p\color{Red}o\color{Red}s\color{Red}i\color{Red}t\color{Red}e\\
& = \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \cos \left( \theta \right) + \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \sin \left( \theta \right)\\
& = x . \cos \left( \theta \right) + y . \sin \left( \theta \right)
\end{align}
$$
</div>

and:

<div>
$$
\begin{align}
y' & = \color{Red}a\color{Red}d\color{Red}j\color{Red}a\color{Red}c\color{Red}e\color{Red}n\color{Red}t - \color{Green}o\color{Green}p\color{Green}p\color{Green}o\color{Green}s\color{Green}i\color{Green}t\color{Green}e\\
& = \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \cos \left( \theta \right) - \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \sin \left( \theta \right)\\
& = y . \cos \left( \theta \right) - x . \sin \left( \theta \right)\\
& = -x . \sin \left( \theta \right) + y . \cos \left( \theta \right)
\end{align}
$$
</div>

In the end what we really have here is a system of equations that we can
represent as a 2x2 matrix:

<div>
$$
\begin{bmatrix}
x'\\
y'
\end{bmatrix}
=
\begin{bmatrix}
\cos \left( \theta \right) & \sin \left( \theta \right)\\
-\sin \left( \theta \right) & \cos \left( \theta \right)
\end{bmatrix}
.
\begin{bmatrix}
x\\
y
\end{bmatrix}
$$
</div>

But this is not *exactly* what we are looking for, right?  
This defines the relationship to convert from the *new* coordinates in the
original plane
`$ \left(x, y\right) $`
what are the coordinates `$ \left(x', y'\right) $` in the rotated plane.  
Whereas what we want to define is how to convert from the rotated plane
(the coordinates that we know) to the original plane.

In order to do what we want, we need to take the same matrix, but define a
rotation of `$ - \theta $`.

We know that:

<div>
$$
\cos \left( -\theta \right) = cos \left( \theta \right)\\
\sin \left( -\theta \right) = - sin \left( \theta \right)
$$
</div>

Which gives us our desired rotation matrix:

<div>
$$
\begin{bmatrix} a & b\\c & d \end{bmatrix}
=
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right)\\
\sin \left( \theta \right) & \cos \left( \theta \right)
\end{bmatrix}
$$
</div>

Congratulations! You know of to define scaling, reflexion, shearing and rotation
transformation matrices. So what is missing?

## 2D transformations with 3x3 matrices

If you're still with me at this point, maybe you're wondering why any of this
is useful. If it's the case, you missed the point of this article, which is to
***understand*** affine transformations in order to apply them in code.

This is useful because at this point you know what a transformation matrix
looks like, and you know how to compute one given a few position vectors,
and it is also a great accomplishment by itself.

But here's the thing: 2x2 matrices are limiting us in the number of operations
we can perform. With a 2x2 matrix, the only transformations we can do are the
ones we've seen in the previous section:

* Scaling
* Reflexion
* Shearing
* Rotation

So what are we missing? Answer: translations!  
And this is unfortunate, as translations are really useful, like when the user
pans and the image has to behave accordingly (aka. *follow the finger*).  
Translations are defined by the addition of two matrices :

<div>
$$
\begin{bmatrix}
x'\\
y'
\end{bmatrix}
=
\begin{bmatrix}
x\\
y
\end{bmatrix}
+
\begin{bmatrix}
t_{x}\\
t_{y}
\end{bmatrix}
$$
</div>

But we want our user to be able to combine/chain transformations (like
zooming on a specific point which is not the origin), so we need to find a
way to express translations as matrices multiplications too.

Here comes the world of [Homogeneous coordinates][homogeneous-coordinates]...

**No, you don't *have* to read it**, and no I don't totally get it either...

The gist of it is:

* the Cartesian plane you're used to, is really just one of the multiple
  planes that exist in the 3D space, and is at `$ z = 1 $`
* for any point `$ \left(x, y, z\right)$` in the 3D space, the line in
  the projecting space that is going through this point and the origin is
  also passing through any point that is obtained by scaling
  `$x$`, `$y$` and `$z$` by the same factor
* the coordinates of any of these points on the line is
  `$ \left(\frac{x}{z}, \frac{y}{z}, z\right)$`.

`TODO` Insert projective geometry/homogeneous coordinates 3D plot here

I've collected a list of blog posts, articles and videos links at the end of
this post if you're interested.

Without further dig in, this is helping, because it says that we
can now represent any point in our Cartesian plane (`$ z = 1 $`) not only as
a 2x1 matrix, but also as a 3x1 matrix:

<div>
$$
\begin{bmatrix}
x\\
y
\end{bmatrix}
\Leftrightarrow
\begin{bmatrix}
x\\
y\\
1
\end{bmatrix}
$$
</div>

Which means we have to redefine all our previous transformation matrices,
because the product of a 3x1 matrix (position vector) by a 2x2 matrix
(transformation) is *undefined*.

**Don't rage quit! It's straightforward: `$z'=z$`.**

We have to find the transformation matrix `$
\mathbf{A} =
\begin{bmatrix} a & b & c\\ d & e & f\\ g & h & i \end{bmatrix}
$`

If, like in the previous section, we imagine that we have the point
`$ P_{\left(x, y, z\right)} $`, which represents any point of
an object on the cartesian plane, then we want to find the matrix to transform
it into `$ P'_{\left(x', y', z'\right)}$` such that

<div>
$$
\begin{bmatrix} x'\\y'\\z' \end{bmatrix} =
\mathbf{A} . \begin{bmatrix} x\\y\\z \end{bmatrix} =
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
.
\begin{bmatrix} x\\y\\z \end{bmatrix}
$$
</div>


### Scaling

<section class="split-third">
    <div class="left">
$$
x' = s_{x} . x\\
\Rightarrow
s_{x} . x = a . x + b . y + c . z\\
\Rightarrow
a = s_{x} \text{ and } b = 0 \text{ and } c = 0
$$
    </div>
    <div class="middle">
$$
y' = s_{y} . y\\
\Rightarrow
s_{y} . y = d . x + e . y + f + z\\
\Rightarrow
d = s_{y} \text{ and } e = 0 \text{ and } f = 0
$$
    </div>
    <div class="right">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i + z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
    </div>
</section>

<br>

The 3x3 scaling matrix for the factors
`$ \left(s_{x}, s_{y}\right) $` is

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix} s_{x} & 0 &0\\0 & s_{y} & 0\\0 & 0 & 1\end{bmatrix}
$$
</div>

### Reflexion

Reflexion around the `$x$` axis:

<section class="split-third">
    <div class="left">
$$
x' = x\\
\Rightarrow
x = a . x + b . y + c . z\\
\Rightarrow
a = 1 \text{ and } b = 0 \text{ and } c = 0
$$
</div>
    <div class="middle">
$$
y' = -y\\
\Rightarrow
-y = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = -1 \text{ and } f = 0
$$
</div>
    <div class="right">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</section>

<br>

The transformation matrix to reflect around the `$x$` axis is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix}
1 & 0 & 0\\
0 & -1 & 0\\
0 & 0 & 1
\end{bmatrix}
$$
</div>

Reflexion around the `$y$` axis:

<section class="split-third">
    <div class="left">
$$
x' = -x\\
\Rightarrow
-x = a . x + b . y + c . z\\
\Rightarrow
a = -1 \text{ and } b = 0 \text{ and } c = 0
$$
</div>
    <div class="middle">
$$
y' = y\\
\Rightarrow
y = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = 1 \text{ and } f = 0
$$
</div>
    <div class="right">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</section>

<br>

The transformation matrix to reflect around the `$y$` axis is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix}
-1 & 0 & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{bmatrix}
$$
</div>

### Shearing

<section class="split-half">
    <div class="left">
$$
x' = x + y . \tan \left( -\alpha \right) \\
\Rightarrow
x + y . \tan \left( -\alpha \right) = a . x + b . y + c . z\\
\Rightarrow
a = 1 \text{ and } b = \tan \left( -\alpha \right) \text{ and } c = 0
$$
</div>
    <div class="right">
$$
y' = y\\
\Rightarrow
y = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = 1 \text{ and } f = 0
$$
</div>
</section>

<br>

<section class="split-half">
    <div class="right">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</section>

The transformation matrix to shear along the `$x$` direction is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix}
1 & \tan \left( -\alpha \right) & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{bmatrix}
=
\begin{bmatrix}
1 & k_{x} & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{bmatrix}\\
\text{where } k \text{ is the shearing constant}
$$
</div>

Similarly, the transformation matrix to shear along the `$y$` direction is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix}
1 & 0 & 0\\
\tan \left( -\beta \right) & 1 & 0\\
0 & 0 & 1
\end{bmatrix}
=
\begin{bmatrix}
1 & 0 & 0\\
k_{y} & 1 & 0\\
0 & 0 & 1
\end{bmatrix}\\
\text{where } k \text{ is the shearing constant}
$$
</div>

### Rotating

Well, I'm a bit lazy here 🤗  
You see the pattern, right?

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & 0\\
\sin \left( \theta \right) & \cos \left( \theta \right) & 0\\
0 & 0 & 1
\end{bmatrix}
$$
</div>

#### Translation

And now we can define translations as matrices multiplication!

<section class="split-third">
    <div class="left">
`$$
x' = x + t_{x} \\
\Rightarrow
x + t_{x} = a . x + b . y + c . z\\
\Rightarrow
a = 1 \text{ and } b = 0 \text{ and } c = t_{x}
$$`
</div>
    <div class="middle">
`$$
y' = y + t_{y}\\
\Rightarrow
y + t_{y} = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = 1 \text{ and } f = t_{y}
$$`
</div>
    <div class="right">
`$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$`
</div>
</section>
<br>

The 3x3 translation matrix for the translation
`$ \left(t_{x}, t_{y}\right) $` is

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix} 1 & 0 & t_{x}\\0 & 1 & t_{y}\\0 & 0 & 1\end{bmatrix}
$$
</div>

## Matrices wrap-up

We went into more or less details in the previous sections.  
Obviously, you won't have to go into all of these algebra stuff each
time you want to know what is the matrix you need to apply in order to do
your transformations.

You can "just" use the following reminder.

Translation matrix:
`$
\begin{bmatrix}
1 & 0 & t_{x}\\
0 & 1 & t_{y}\\
0 & 0 & 1
\end{bmatrix}
$`

Scaling matrix:
`$
\begin{bmatrix}
s_{x} & 0 & 0\\
0 & s_{y} & 0\\
0 & 0 & 1
\end{bmatrix}
$`

Shear matrix:
`$
\begin{bmatrix}
1 & \tan \left( - \alpha \right) & 0\\
\tan \left( - \beta \right) & 1 & 0\\
0 & 0 & 1
\end{bmatrix}
\text{ = }
\begin{bmatrix}
1 & k_{x} & 0\\
k_{y} & 1 & 0\\
0 & 0 & 1
\end{bmatrix}
$`

Rotation matrix:
`$
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & 0\\
\sin \left( \theta \right) & \cos \left( \theta \right) & 0\\
0 & 0 & 1
\end{bmatrix}
$`

That's neat! Now you can define your matrices easily, plus you know how it
works.

One last thing: all the transformations we've seen are ***centered around the
origin***.  
How do we apply what we know in order to, for instance, zoom on a
specific point which is **not** the origin, or rotate an object **in place**,
around its center?

The answer is ***composition***: We must *compose* our transformations by using
several other transformations.

### How to zoom on a specific point

Imagine you have a shape, like a square for instance, and you want to zoom in
at the center of the square, to mimic a pinch-zoom behaviour.  
This transformation is composed of the following sequence:

* move anchor point to origin: `$ \left( -t_{x}, -t_{y} \right) $`
* scale by `$ \left( s_{x}, s_{y} \right) $`
* move back anchor point: `$ \left( t_{x}, t_{y} \right) $`

Where `$t$` is the anchor point of our scaling transformation (the center of
the square).

Our transformations are defined by the first translation matrix
`$ \mathbf{C} $`, the scaling matrix `$ \mathbf{B} $`, and the last
translation matrix `$ \mathbf{A} $`.

<div>
$$
\mathbf{C} =
\begin{bmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{bmatrix}
\text{ , }
\mathbf{B} =
\begin{bmatrix}
s_{x} & 0 & 0 \\
0 & s_{y} & 0 \\
0 & 0 & 1
\end{bmatrix}
\text{ and }
\mathbf{A} =
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
$$
</div>

Because matrix multiplication is non-commutative, the order matters, so we will
apply them in reverse order (hence the reverse naming order).  
The composition of these transformations gives us the following product:

<div>
$$
\begin{align}
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
s_{x} & 0 & 0 \\
0 & s_{y} & 0 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
s_{x} & 0 & s_{x} . -t_{x} \\
0 & s_{y} & s_{y} . -t_{y} \\
0 & 0 & 1
\end{bmatrix}\\
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{bmatrix}
s_{x} & 0 & s_{x} . -t_{x} + t_{x} \\
0 & s_{y} & s_{y} . -t_{y} + t_{y} \\
0 & 0 & 1
\end{bmatrix}
\end{align}
$$
</div>

Suppose we have the following points representing a square:
`$
\begin{bmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{bmatrix}
\text{ = }
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}
$`

`TODO` Insert 2D plot with those 4 points

And we want to apply a 2x zoom focusing on its center.  
The new coordinates will be:

<div>
$$
\begin{align}
\begin{bmatrix}
x_{1}' & x_{2}' & x_{3}' & x_{4}'\\
y_{1}' & y_{2}' & y_{3}' & y_{4}'\\
1 & 1 & 1 & 1
\end{bmatrix}
&=
\begin{bmatrix}
s_{x} & 0 & s_{x} . -t_{x} + t_{x} \\
0 & s_{y} & s_{y} . -t_{y} + t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
2 & 0 & 2 . \left(-3\right) + 3 \\
0 & 2 & 2 . \left(-2\right) + 2 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
2 & 0 & -3 \\
0 & 2 & -2 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}\\
\begin{bmatrix}
x_{1}' & x_{2}' & x_{3}' & x_{4}'\\
y_{1}' & y_{2}' & y_{3}' & y_{4}'\\
1 & 1 & 1 & 1
\end{bmatrix}
&=
\begin{bmatrix}
1 & 5 & 5 & 1\\
0 & 0 & 4 & 4\\
1 & 1 & 1 & 1
\end{bmatrix}
\end{align}
$$
</div>

`TODO` Insert 2D plot with the 8 points

### How to rotate around a specific point

Now imagine you have an image in a view, the origin is not a the center of the
view, it is probably at the top-left corner (implementations may vary),
but you want to rotate the image at the center of the view.  
This transformation is composed of the following sequence:

* move anchor point to origin: `$ \left( -t_{x}, -t_{y} \right) $`
* rotate by `$ \theta $`
* move back anchor point: `$ \left( t_{x}, t_{y} \right) $`

Where `$t$` is the anchor point of our rotation transformation.

Our transformations are defined by the first translation matrix
`$ \mathbf{C} $`, the rotation matrix `$ \mathbf{B} $`, and the last
translation matrix `$ \mathbf{A} $`.

<div>
$$
\mathbf{C} =
\begin{bmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{bmatrix}
\text{ , }
\mathbf{B} =
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & 0 \\
\sin \left( \theta \right) & \cos \left( \theta \right) & 0 \\
0 & 0 & 1
\end{bmatrix}
\text{ and }
\mathbf{A} =
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
$$
</div>

The composition of these transformations gives us the following product:

<div>
$$
\begin{align}
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & 0 \\
\sin \left( \theta \right) & \cos \left( \theta \right) & 0 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & \cos \left( \theta \right) . -t_{x} + \sin \left( \theta \right) . -t_{y} \\
\sin \left( \theta \right) & \cos \left( \theta \right) & \sin \left( \theta \right) . -t_{x} + \cos \left( \theta \right) . -t_{y} \\
0 & 0 & 1
\end{bmatrix}\\
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & \cos \left( \theta \right) . -t_{x} - \sin \left( \theta \right) . -t_{y} + t_{x} \\
\sin \left( \theta \right) & \cos \left( \theta \right) & \sin \left( \theta \right) . -t_{x} + \cos \left( \theta \right) . -t_{y} + t_{y} \\
0 & 0 & 1
\end{bmatrix}
\end{align}
$$
</div>


Suppose we have the following points representing a square:
`$
\begin{bmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{bmatrix}
\text{ = }
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}
$`

`TODO` Insert 2D plot with those 4 points

And we want to apply a rotation of `$ \theta = 90° $` focusing on its center.  
The new coordinates will be:

<div>
$$
\begin{align}
\begin{bmatrix}
x_{1}' & x_{2}' & x_{3}' & x_{4}'\\
y_{1}' & y_{2}' & y_{3}' & y_{4}'\\
1 & 1 & 1 & 1
\end{bmatrix}
&=
\begin{bmatrix}
\cos \left( \theta \right) & -\sin \left( \theta \right) & \cos \left( \theta \right) . -t_{x} - \sin \left( \theta \right) . -t_{y} + t_{x} \\
\sin \left( \theta \right) & \cos \left( \theta \right) & \sin \left( \theta \right) . -t_{x} + \cos \left( \theta \right) . -t_{y} + t_{y} \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
0 & -1 & 0 . \left(-3\right) - 1 . \left(-2\right) + 3 \\
1 & 0 & 1 . \left(-3\right) + 0 . \left(-2\right) + 2 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}\\
&=
\begin{bmatrix}
0 & -1 & 5 \\
1 & 0 & -1 \\
0 & 0 & 1
\end{bmatrix}
.
\begin{bmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{bmatrix}\\
\begin{bmatrix}
x_{1}' & x_{2}' & x_{3}' & x_{4}'\\
y_{1}' & y_{2}' & y_{3}' & y_{4}'\\
1 & 1 & 1 & 1
\end{bmatrix}
&=
\begin{bmatrix}
4 & 4 & 2 & 2\\
1 & 3 & 3 & 1\\
1 & 1 & 1 & 1
\end{bmatrix}
\end{align}
$$
</div>

`TODO` Insert 2D plot with those 8 points

----

{{< img src="/img/matrices-for-developers/koalifications.jpg"
alt="Koala meme">}}

----

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

{{< img src="/img/matrices-for-developers/java-zoom-at-center.png"
title="Demo of Java 2D zooming: in black the original square, in red the unexpected transformation and in green the desired transformation"
alt="Demo of Java 2D zooming" width="100%">}}

## Affine transformations with Android

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

* Igor Laborie (@ilaborie)
* Hadrien Toma (@twitter)

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
