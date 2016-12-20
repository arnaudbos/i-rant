---
title: Matrices for developers
date: 2016-11-19T15:08:29+01:00
description:
tags: ["math","matrix"]
categories: ["math"]
draft: true
highlight: true
math: true
klipse: true
gallery: true
---

A few weeks ago I was on an `android-user-group` channel,
when someone posted a question about Android's
[Matrix.postScale(sx, sy, px, py)][Matrix.postScale] method and how it works
because it was *"hard to grasp"*.

Coincidence: a few months ago, at the beginning of 2016, I finished a freelance
project on an [Android application][climbing-away] where I had to implement
an exciting feature:

The user, after buying and downloading a digital topography of a crag, had to
be able to view the crag which was composed of:

* a picture of the cliff,
* a SVG file containing an overlay of the climbing routes.

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
* What should I use, `preScale` or `postScale`? Do I try both while I get the
  input parameters from my gesture detection code and enter an infite loop of
  trial and error guesstimates? (No. Fucking. Way.)

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


## What is a matrix?

The first resource you might encounter when trying to understand 2D
transformations are articles about *"Transformation matrix"* and
*"Affine transformations"* on Wikipedia:

* https://en.wikipedia.org/wiki/Transformation_matrix
* https://en.wikipedia.org/wiki/Transformation_matrix#Affine_transformations
* https://en.wikipedia.org/wiki/Affine_transformation

I don't know you, but with this material, I almost got everything — wait...

{{< img src="/img/matrices-for-developers/chewbacca-defense.jpg"
alt="Chewbacca defense - It does not make sense" width="80%" center="true"
link="https://en.wikipedia.org/wiki/Chewbacca_defense">}}

**NOPE! I didn't get anything at all.**

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
about a `$m\text{×}n$` matrix relates to an array of length `$m$` in which
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
or subtracting the corresponding entries of the operand matrices:

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

Corollary to this definition we can deduce that in order to be *`defined`*, a matrix
addition or subtraction must be performed against two matrices of same
dimensions `$m\text{×}n$`, otherwise the *"corresponding entries"* bit would
have no sense:  
Grab a pen and paper and try to add a `$3\text{×}2$` matrix to a `$2\text{×}3$`
matrix. What will you do with the last *row* of the first matrix? Same question
with the last *column* of the second matrix?  
If you don't know, then you've reach the same conclusion as the mathematicians
that defined matrices additions and subtractions, pretty much 😋

<h4>Examples</h4>

<div class="row row-same-height">
<div class="col-md-6">
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
<div class="col-md-6">
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
</div>

<br>

### Matrix multiplication

`TODO` Remove this:

{{< klipse >}}

Throughout all my math schooling I've been said things like
*"[you can't add apples to oranges][wiki-apples-oranges], it makes no
sense"*, in order to express the importance of units.  
Well it turns out that multiplying apples and oranges *is* allowed.  
And it can be applied to matrices: we can only add matrices to matrices, but
we can multiply matrices by numbers and by other matrices.

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
entries. I'll just [quote wikipedia][wiki-matrix-multi] on that one:

> if `$\mathbf{A}$` is an `$m\text{×}n$` matrix and `$\mathbf{B}$` is an
> `$n\text{×}p$` matrix, their matrix product `$\mathbf{AB}$` is an
> `$m\text{×}p$` matrix, in which the `$n$` entries across a
> row of `$\mathbf{A}$` are multiplied with the `$n$` entries down a columns
> of `$\mathbf{B}$` and summed to produce an entry of `$\mathbf{AB}$`

This hurts my brain, let's break it down.

The dimensions stuff first:

> if `$\mathbf{A}$` is an `$m\text{×}n$` matrix and `$\mathbf{B}$` is an
> `$n\text{×}p$` matrix, their matrix product `$\mathbf{AB}$` is an
> `$m\text{×}p$` matrix

We can write this in a more graphical way: `$
\underset{m\text{×}n}{\mathbf{A}}
\text{×}
\underset{n\text{×}p}{\mathbf{B}} =
\underset{m\text{×}p}{\mathbf{AB}}
$`.

See this simple matrix
`$\underset{2\text{×}2}{\mathbf{A}} = \begin{bmatrix}a_{11} & a_{12}\\a_{21} & a_{22}\end{bmatrix}$`
and this other matrix
`$\underset{2\text{×}2}{\mathbf{B}} = \begin{bmatrix}b_{11} & b_{12}\\b_{21} & b_{22}\end{bmatrix}$`.  
We have `$m=2$`, `$n=2$` and `$p=2$` so the multiplication will give
`$\underset{2\text{×}2}{\mathbf{AB}} = \begin{bmatrix}ab_{11} & ab_{12}\\ab_{21} & ab_{22}\end{bmatrix}$`.

Let's decompose the second part now:

* *"the `$n$` entries across a row of `$\mathbf{A}$`"* means is that each row
  in `$\mathbf{A}$` is an array of `$n=2$` entries: if we take the first row we
  get `$a_{11}$` and `$a_{12}$`
* *"the `$n$` entries down a columns of `$\mathbf{B}$`"* means that each
  column of `$\mathbf{B}$` is also an array of `$n=2$` entries: in the first
  column we get `$b_{11}$` and `$b_{21}$`.
* *"are multiplied with"* means that each entry in `$\mathbf{A}$`'s row
  must be multiplied with its corresponding (first with first, second with
  second, etc.) entry in `$\mathbf{B}$`'s column: `$a_{11}\text{×}b_{11}$` and
  `$a_{12}\text{×}b_{21}$`
* *"And summed to produce an entry of `$\mathbf{AB}$`"* means that we must add
  the products of these corresponding rows and columns entries in order to get
  the entry of the new matrix at this row number and column number: in our case
  we took the products of the entries in the first row in the first matrix with
  the entries in the first column in the second matrix, so this will give us the
  entry in the first row and first column of the new matrix:
  `$a_{11}\text{×}b_{11} + a_{12}\text{×}b_{21}$`

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

<div class="row row-same-height">
<div class="col-md-4">
<a title="By File:Matrix multiplication diagram.svg:User:Bilou See below. [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons" href="https://commons.wikimedia.org/wiki/File%3AMatrix_multiplication_diagram_2.svg" style="text-align: center;"><img width="256" alt="Matrix multiplication diagram 2" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Matrix_multiplication_diagram_2.svg/256px-Matrix_multiplication_diagram_2.svg.png"/></a>
</div>
<div class="col-md-8">
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
4\text{×}7+3\text{×}\left(-2\right)               & 4\text{×}1+3\text{×}4               & 4\text{×}3+3\text{×}1\\
0\text{×}7+\left(-5\right)\text{×}\left(-2\right) & 0\text{×}1+\left(-5\right)\text{×}4 & 0\text{×}3+\left(-5\right)\text{×}1\\
2\text{×}7+1\text{×}\left(-2\right)               & 2\text{×}1+1\text{×}4               & 2\text{×}3+1\text{×}1\\
\left(-6\right)\text{×}7+8\text{×}\left(-2\right) & \left(-6\right)\text{×}1+8\text{×}4 & \left(-6\right)\text{×}3+8\text{×}1
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
</div>

Remember:

> In order for matrix multiplication to be defined, the number of columns in
> the first matrix must be equal to the number of rows in the second matrix.

Otherwise you **can't** multiply, period.

More details [here][khan-defined-vid] and [here][khan-defined-course] if you
are interested.

## Transformation matrices

Now that we know what is a matrix and how we can multiply matrices, we can see
why it is interesting for **2D transformations**.

### Transforming points

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
and a vector is just a `$2\text{×}1$` matrix.

What we have here, is a matrix multiplication that represents the
transformation of a point into another point. We don't know what the first
point's coordinates are yet, and it doesn't matter. What I wanted to show is
that, given a position vector, we are able to *transform* it into another via
a matrix multiplication operation.

Given a point `$P$`, whose coordinates are represented by the position vector,
`$\begin{bmatrix}x\\y\end{bmatrix}$`, we can obtain a new point `$P'$` whose
coordinates are represented by the position vector
`$\begin{bmatrix}x'\\y'\end{bmatrix}$` by multiplying it by a matrix.

One important thing is that this
*[transformation matrix][khan-transf-vector]* has to have specific
dimensions, in order to fulfill the rule of matrix multiplication: because
`$\begin{bmatrix}x\\y\end{bmatrix}$` is a `$2\text{×}1$` matrix, and
`$\begin{bmatrix}x'\\y'\end{bmatrix}$` is also a `$2\text{×}1$` matrix, then the
transformation matrix has to be a `$2\text{×}2$` matrix in order to have:

<div>
$$
\mathbf{A}
.
\begin{bmatrix}
x\\y
\end{bmatrix}
=
\begin{bmatrix}
a_{11} & a_{12}\\
a_{21} & a_{22}
\end{bmatrix}
.
\begin{bmatrix}
x\\y
\end{bmatrix}
=
\begin{bmatrix}
x'\\y'
\end{bmatrix}
$$
</div>

> **Note:** The order here is important as we will see later, but you can
> already see that switching `$\mathbf{A}$` and
> `$\begin{bmatrix}x\\y\end{bmatrix}$` would lead to an `$undefined$` result
> (if you don't get it, re-read the part on matrix multiplication and their
> dimensions).

Notice that the nature of the transformation represented by our matrix above
and in the link is not clear, and I didn't say what kind of transformation it
is, on purpose. The transformation matrix was picked at random, and yet we
see how interesting and useful it is for 2D manipulation of graphics.

> Another great thing about transformation matrices, is that they can be used
> to [transform a whole bunch of points][khan-transf-polygone] at the same time.

For now, I suppose all you know is the type of transformations you want to
apply: `rotation`, `scale` or `translation` and some parameters.

So how do you go from `scale by a factor of 2` and `rotate 90 degrees clockwise`
to a transformation matrix?

Well the answer is:

{{< img src="/img/matrices-for-developers/moar-math-stuff.jpg"
alt="Moar math stuff with smiling cat meme" width="60%" center="true">}}

### More math stuff

More specifically I encourage you to read [this course on *Matrices as
transformations* (which is full of fancy plots and
animations)][khan-matrices-transform] and particularly its last part:
***Representing two dimensional linear transforms with matrices***.

Come back here once you've read it, or it's gonna hurt 😅

---

Ok I suppose you've read the course I linked to above, but just in case, here
is a reminder

* a position vector `$ \begin{bmatrix} x\\ y \end{bmatrix} $` can be broken
  down as
  `$
  \begin{bmatrix} x\\y \end{bmatrix} =
  x \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix}
  +
  y \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix}
  $`.

{{% fold id="transform-matrix-reminder-1" title="Show explanation" %}}
If you decompose `$\begin{bmatrix}x\\y\end{bmatrix}$` into a matrix addition
operation, you find
`$
\begin{bmatrix}x\\y\end{bmatrix} =
\begin{bmatrix}x\\0\end{bmatrix} + \begin{bmatrix}0\\y\end{bmatrix}$`.

And if you decompose a little bit more you can express each operand of
this addition as the multiplication of a scalar and a matrix:

* `$\begin{bmatrix}x\\0\end{bmatrix} = x . \begin{bmatrix}1\\0\end{bmatrix}$`
* `$\begin{bmatrix}0\\y\end{bmatrix} = y . \begin{bmatrix}0\\1\end{bmatrix}$`

Now look at the the matrices `$\begin{bmatrix}1\\0\end{bmatrix}$` and
`$\begin{bmatrix}0\\1\end{bmatrix}$`, they are the Cartesian unit vectors.

So
`$
\begin{bmatrix} x\\y \end{bmatrix} =
x \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix}
+
y \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix} $` is just another
way to write that the position vector `$\begin{bmatrix} x\\ y \end{bmatrix}$`
represents a point given by a transformation of the *unit vectors* of our
Cartesian plane.
{{% /fold %}}

* `$ \begin{bmatrix} \color{Green} a\\ \color{Green} c \end{bmatrix} $` and
  `$ \begin{bmatrix} \color{Red} b\\ \color{Red} d \end{bmatrix} $` are the
  position vectors where
  `$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 1 \end{bmatrix} $` and
  `$ \begin{bmatrix} \color{Red} 1\\ \color{Red} 0 \end{bmatrix} $` will land
  respectively after the transformation matrix
  `$
  \mathbf{A} =
  \begin{bmatrix}
  \color{Green} a & \color{Red} b\\
  \color{Green} c & \color{Red} d
  \end{bmatrix}
  $` has been applied.

{{% fold id="transform-matrix-reminder-2" title="Show explanation" %}}
Let's start again from our unit vectors
`$ \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix} $`
and
`$ \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix} $`.

We know that
`$
\begin{bmatrix} x\\y \end{bmatrix} =
x \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix}
+
y \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix}
$`, so now imagine we apply a transformation to our plane.

Our unit vectors will be transformed too, right?

If we assume that
`$ \begin{bmatrix} \color{Green} 1\\ \color{Green} 0 \end{bmatrix} $`
*"lands on"*
`$ \begin{bmatrix} \color{Green} a\\ \color{Green} c \end{bmatrix} $`
and that
`$ \begin{bmatrix} \color{Red} 0\\ \color{Red} 1 \end{bmatrix} $`
*"lands on"*
`$ \begin{bmatrix} \color{Red} b\\ \color{Red} d \end{bmatrix} $`,  
then we have our position vector `$ \begin{bmatrix} x\\y \end{bmatrix} $`
landing on
`$
x \begin{bmatrix} \color{Green} a\\ \color{Green} c \end{bmatrix}
+ y \begin{bmatrix} \color{Red} b\\ \color{Red} d \end{bmatrix}
 =
\begin{bmatrix}
\color{Green} a x + \color{Red} b y\\
\color{Green} c x + \color{Red} d y
\end{bmatrix}
$`.
{{% /fold %}}

* given the previous transformation,
  `$ \begin{bmatrix} x\\ y \end{bmatrix} $` will land on
  `$
  \begin{bmatrix}
  \color{Green} a x + \color{Red} b y\\
  \color{Green} c x + \color{Red} d y
  \end{bmatrix}
  $`.

If you don't understand this conclusion, read again, read the course, take
your time.

----

Now remember, our goal is to determine what `$ \mathbf{A} $` is, because we
know the transformation we want to apply but we're searching for the matrix we
should apply to our position vector(s) in order to transform our graphics.

Let's take the example of the transformation of a series of points: we know
where the position vectors will land, but we're looking for `$ \mathbf{A} $`.

`TODO` Insert 2D plane with `$P_{(2,1)}$`, `$Q_{(-2,0)}$`, `$P'_{(5, 0)}$`
and `$Q'_{(-4, 2)}$`.

We know that:

<div class="row row-same-height">
    <div class="col-md-6">$$ \begin{bmatrix} 2\\ 1 \end{bmatrix} \text{ lands on } \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="col-md-6">$$ \begin{bmatrix} -2\\ 0 \end{bmatrix} \text{ lands on } \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</div>

<br>

Which means:

<div class="row row-same-height">
    <div class="col-md-6">$$ \begin{bmatrix} x\\ y \end{bmatrix} = \begin{bmatrix} 2\\ 1 \end{bmatrix} \text{ lands on } \begin{bmatrix} a.x+b.y\\ c.x+d.y \end{bmatrix} = \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="col-md-6">$$ \begin{bmatrix} x\\ y \end{bmatrix} = \begin{bmatrix} -2\\ 0 \end{bmatrix} \text{ lands on } \begin{bmatrix} a.x+b.y\\ c.x+d.y \end{bmatrix} = \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</div>

<br>

From which we can deduce:

<div class="row row-same-height">
    <div class="col-md-6">$$ \begin{bmatrix} 2.a+1.b\\ 2.c+1.d \end{bmatrix} = \begin{bmatrix} 5\\ 0 \end{bmatrix} $$</div>
    <div class="col-md-6">$$ \begin{bmatrix} -2.a+0.b\\ -2.c+0.d \end{bmatrix} = \begin{bmatrix} -4\\ 2 \end{bmatrix} $$</div>
</div>

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

This matrix is called the [identity matrix][khan-identity-matrix].

{{% fold id="identity-matrix" title="More on the identity matrix" %}}
{{< youtube 3cnIa0fYJkY >}}
{{% /fold %}}

### Combining transformations

One more thing before we get concrete: *We want our user to be able
to combine/chain transformations* (like zooming and panning at the same time
for instance).

In order to chain multiple transformations we need to understand the
[properties of matrix multiplication][khan-mult-properties], and more
specifically the `non-commutative` and `associative` properties of matrix
multiplication:

* Matrix multiplication is associative
  `$\left(\mathbf{A}.\mathbf{B}\right).\mathbf{C} = \mathbf{A}.\left(\mathbf{B}.\mathbf{C}\right)$`

{{% fold id="matrix-properties-1" title="Show explanation" %}}
Just trust me already!

If you don't, I'm not going to write it here because it takes a lot of screen
width (I've tried and it didn't render very well), so check out this video.

{{< youtube 8Ryfe82DTcM >}}
{{% /fold %}}

* Matrix multiplication is non-commutative
  `$\mathbf{A}.\mathbf{B} \neq \mathbf{B}.\mathbf{A}$`

{{% fold id="matrix-properties-2" title="Show explanation" %}}
In order to affirm this we just have to prove commutativity wrong, which is
easy!

Imagine `$\mathbf{A}$` is a `$5\text{×}2$` matrix, and `$\mathbf{B}$` is a
`$2\text{×}3$` matrix:

* `$\underset{5\text{×}2}{\mathbf{A}}.\underset{2\text{×}3}{\mathbf{B}}=\underset{5\text{×}3}{\mathbf{C}}$`
* `$\underset{2\text{×}3}{\mathbf{B}}.\underset{5\text{×}2}{\mathbf{A}}=undefined$`

And that's it. But we can also see commutativity does not hold even for
matrices of same dimensions:

<div class="row row-same-height">
  <div class="col-md-6">
$$
\mathbf{A}.\mathbf{B}\\
=
\begin{bmatrix}
a_{11} & a_{12}\\
a_{21} & a_{22}
\end{bmatrix}
.
\begin{bmatrix}
b_{11} & b_{12}\\
b_{21} & b_{22}
\end{bmatrix}\\
=
\begin{bmatrix}
a_{11}.b_{11}+a_{12}.b_{21} & a_{11}.b_{12}+a_{12}.b_{22}\\
a_{21}.b_{11}+a_{22}.b_{21} & a_{11}.b_{22}+a_{22}.b_{22}
\end{bmatrix}
$$
  </div>
  <div class="col-md-6">
$$
\mathbf{B}.\mathbf{A}\\
=
\begin{bmatrix}
b_{11} & b_{12}\\
b_{21} & b_{22}
\end{bmatrix}
.
\begin{bmatrix}
a_{11} & a_{12}\\
a_{21} & a_{22}
\end{bmatrix}\\
=
\begin{bmatrix}
b_{11}.a_{11}+b_{12}.a_{21} & b_{11}.a_{12}+b_{12}.a_{22}\\
b_{21}.a_{11}+b_{22}.a_{21} & b_{21}.a_{12}+b_{22}.a_{22}
\end{bmatrix}
$$
  </div>
</div>

Grab a pen and paper and try it for yourself with the following matrices
`$\mathbf{A}=\begin{bmatrix}1 & 2\\-3 & -4\end{bmatrix}$` and
`$\mathbf{B}=\begin{bmatrix}-2 & 0\\0 & -3\end{bmatrix}$`.
{{% /fold %}}

Back to our transformations.  
Imagine we want to apply transformation `$ \mathbf{B} $`, then transformation
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

We know that matrix multiplication is `associative`, which gives us:


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

We also know that matrix multiplication is `not commutative`, so the order
in which we multiply our transformation matrices
(`$ \mathbf{A} . \mathbf{B} $` or `$ \mathbf{B} . \mathbf{A} $`) will have
an impact on our final matrix and will lead to different results, different
transformations.

## Types of transformations

There are several types of 2D transformations we are able to define using
`$2\text{×}2$` dimensions matrices, and you've had a preview of most of them
in this course on [matrices as transformations][khan-matrices-transform].  
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

<div class="row row-same-height">
    <div class="col-md-6">
$$
s_{x} . x = a . x + b . y\\
\Rightarrow
a = s_{x} \text{ and } b = 0
$$
</div>
    <div class="col-md-6">
$$
s_{y} . y = c . x + d . y\\
\Rightarrow
c = s_{y} \text{ and } d = 0
$$
</div>
</div>

<br>

In conclusion, the `$2\text{×}2$` scaling matrix for the factors
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

<div class="row row-same-height">
    <div class="col-md-6">
$$
x' = x\\
\Rightarrow
x = a . x + b . y\\
\Rightarrow
a = 1 \text{ and } b = 0
$$
</div>
    <div class="col-md-6">
$$
y' = -y\\
\Rightarrow
-y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = -1
$$
</div>
</div>

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

<div class="row row-same-height">
    <div class="col-md-6">
$$
x' = -x\\
\Rightarrow
-x = a . x + b . y\\
\Rightarrow
a = -1 \text{ and } b = 0
$$
</div>
    <div class="col-md-6">
$$
y' = y\\
\Rightarrow
y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = 1
$$
</div>
</div>

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
function to find the opposite side's length.

<div class="row row-same-height">
    <div class="col-md-6">
$$
x' = x + y . \tan \left( -\alpha \right) \\
\Rightarrow
x + y . \tan \left( -\alpha \right) = a . x + b . y\\
\Rightarrow
a = 1 \text{ and } b = \tan \left( -\alpha \right)
$$
</div>
    <div class="col-md-6">
$$
y' = y\\
\Rightarrow
y = c . x + d . y\\
\Rightarrow
c = 0 \text{ and } d = 1
$$
</div>
</div>

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

<div class="row row-same-height">
<div class="col-md-6">
$$
\begin{align}
x' & = \color{Green}a\color{Green}d\color{Green}j\color{Green}a\color{Green}c\color{Green}e\color{Green}n\color{Green}t + \color{Red}o\color{Red}p\color{Red}p\color{Red}o\color{Red}s\color{Red}i\color{Red}t\color{Red}e\\
& = \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \cos \left( \theta \right) + \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \sin \left( \theta \right)\\
& = x . \cos \left( \theta \right) + y . \sin \left( \theta \right)
\end{align}
$$
</div>
<div class="col-md-6">
$$
\begin{align}
y' & = \color{Red}a\color{Red}d\color{Red}j\color{Red}a\color{Red}c\color{Red}e\color{Red}n\color{Red}t - \color{Green}o\color{Green}p\color{Green}p\color{Green}o\color{Green}s\color{Green}i\color{Green}t\color{Green}e\\
& = \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \cos \left( \theta \right) - \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \sin \left( \theta \right)\\
& = y . \cos \left( \theta \right) - x . \sin \left( \theta \right)\\
& = -x . \sin \left( \theta \right) + y . \cos \left( \theta \right)
\end{align}
$$
</div>
</div>

In the end what we really have here is a system of equations that we can
represent as a `$2\text{×}2$` matrix:

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

## 3x3 transformation matrices

If you're still with me at this point, maybe you're wondering why any of this
is useful. If it's the case, you missed the point of this article, which is to
***understand*** affine transformations in order to apply them in code.

This is useful because at this point you know what a transformation matrix
looks like, and you know how to compute one given a few position vectors,
and it is also a great accomplishment by itself.

But here's the thing: `$2\text{×}2$` matrices are limiting us in the number of operations
we can perform. With a `$2\text{×}2$` matrix, the only transformations we can do are the
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

* the Cartesian plane you're used to, is really just one of many
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
a `$2\text{×}1$` matrix, but also as a `$3\text{×}1$` matrix:

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
because the product of a `$3\text{×}1$` matrix (position vector) by a
`$2\text{×}2$` matrix (transformation) is *`undefined`*.

**Don't rage quit! It's straightforward: `$\mathbf{z'=z}$`!**

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

We are looking for `$\mathbf{A}$` such that:

<div>
$$
\begin{bmatrix} x'\\y'\\z' \end{bmatrix} =
\begin{bmatrix} s_{x}.x\\s_{y}.y\\z \end{bmatrix} =
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
.
\begin{bmatrix} x\\y\\z \end{bmatrix}
$$
</div>

We can solve the following system of equation in order to find `$\mathbf{A}$`:

<div class="row row-same-height">
    <div class="col-md-4">
$$
x' = s_{x} . x\\
\Rightarrow
s_{x} . x = a . x + b . y + c . z\\
\Rightarrow
a = s_{x} \text{ and } b = 0 \text{ and } c = 0
$$
    </div>
    <div class="col-md-4">
$$
y' = s_{y} . y\\
\Rightarrow
s_{y} . y = d . x + e . y + f + z\\
\Rightarrow
d = s_{y} \text{ and } e = 0 \text{ and } f = 0
$$
    </div>
    <div class="col-md-4">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i + z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
    </div>
</div>

<br>

The 3x3 scaling matrix for the factors
`$ \left(s_{x}, s_{y}\right) $` is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix} s_{x} & 0 &0\\0 & s_{y} & 0\\0 & 0 & 1\end{bmatrix}
$$
</div>

### Reflexion

For a reflexion around the `$x$` axis we are looking for `$\mathbf{A}$` such
that:

<div>
$$
\begin{bmatrix} x'\\y'\\z' \end{bmatrix} =
\begin{bmatrix} x\\-y\\z \end{bmatrix} =
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
.
\begin{bmatrix} x\\y\\z \end{bmatrix}
$$
</div>

We can solve the following system of equation in order to find `$\mathbf{A}$`:

<div class="row row-same-height">
    <div class="col-md-4">
$$
x' = x\\
\Rightarrow
x = a . x + b . y + c . z\\
\Rightarrow
a = 1 \text{ and } b = 0 \text{ and } c = 0
$$
</div>
    <div class="col-md-4">
$$
y' = -y\\
\Rightarrow
-y = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = -1 \text{ and } f = 0
$$
</div>
    <div class="col-md-4">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</div>

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

For the reflexion around the `$y$` axis we are looking for `$\mathbf{A}$` such
that:

<div>
$$
\begin{bmatrix} x'\\y'\\z' \end{bmatrix} =
\begin{bmatrix} x\\-y\\z \end{bmatrix} =
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
.
\begin{bmatrix} x\\y\\z \end{bmatrix}
$$
</div>

We can solve the following system of equation in order to find `$\mathbf{A}$`:

<div class="row row-same-height">
    <div class="col-md-4">
$$
x' = -x\\
\Rightarrow
-x = a . x + b . y + c . z\\
\Rightarrow
a = -1 \text{ and } b = 0 \text{ and } c = 0
$$
</div>
    <div class="col-md-4">
$$
y' = y\\
\Rightarrow
y = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = 1 \text{ and } f = 0
$$
</div>
    <div class="col-md-4">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</div>

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

Well, I'm a bit lazy here 🤗  
You see the pattern, right? Third line always the same, third column always the
same.

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

Same pattern, basically we just take the `$2\text{×}2$` rotation matrix
and add one row and one column whose entries are `$0$`, `$0$` and `$1$`.

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

### Translation

And now it gets interesting, because we can define translations as
`$3\text{×}3$` matrices multiplication!

We are looking for `$\mathbf{A}$` such that:

<div>
$$
\begin{bmatrix} x'\\y'\\z' \end{bmatrix} =
\begin{bmatrix} x+t_{x}\\y+t_{y}\\z \end{bmatrix} =
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
.
\begin{bmatrix} x\\y\\z \end{bmatrix}
$$
</div>

We can solve the following system of equation in order to find `$\mathbf{A}$`:

<div class="row row-same-height">
    <div class="col-md-4">
$$
x' = x + t_{x} \\
\Rightarrow
x + t_{x} = a . x + b . y + c . z\\
\Rightarrow
a = 1 \text{ and } b = 0 \text{ and } c = t_{x}
$$
</div>
    <div class="col-md-4">
$$
y' = y + t_{y}\\
\Rightarrow
y + t_{y} = d . x + e . y + f . z\\
\Rightarrow
d = 0 \text{ and } e = 1 \text{ and } f = t_{y}
$$
</div>
    <div class="col-md-4">
$$
z' = z\\
\Rightarrow
z = g . x + h . y + i . z\\
\Rightarrow
g = 0 \text{ and } h = 0 \text{ and } i = 1
$$
</div>
</div>

The `$3\text{×}3$` translation matrix for the translation
`$ \left(t_{x}, t_{y}\right) $` is:

<div>
$$
\begin{bmatrix} a & b & c\\d & e & f\\g & h & i\end{bmatrix}
=
\begin{bmatrix} 1 & 0 & t_{x}\\0 & 1 & t_{y}\\0 & 0 & 1\end{bmatrix}
$$
</div>

## Matrices wrap-up

Obviously, you won't have to go into all of these algebra stuff each
time you want to know what is the matrix you need to apply in order to do
your transformations.

You can just use the following:

### Reminder

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

### Combination use-case: pinch-zoom

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

### Combination use-case: rotate image

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

## Acknowledgements

I want to address my warmest thank you to the following people, who helped me
during the review process of this article, by providing helpful feedbacks and
advices:

* Igor Laborie ([@ilaborie][ilaborie])
* Hadrien Toma

## Links

* [A course on "Affine Transformation" at The University of Texas at Austin][matrices-texas]
* [A course on "Composing Transformations" at The Ohio State University][ohio-matrices]
* [A blogpost on "Rotating images" by Nick Berry][datagenetics-rotation]
* [A Youtube video course on "The Rotation Matrix" by Michael J. Ruiz][matrix-rotation-video]
* [Wikipedia on Homogeneous coordinates][homogeneous-coordinates]
* [A blogpost on "Explaining Homogeneous Coordinates & Projective Geometry" by Tom Dalling][explaining-homogenous-coordinates]
* [A blogpost on "Homogeneous Coordinates" by Song Ho Ahn][homogeneous-coordinates-blogpost]
* [A Youtube video course on "2D transformations and homogeneous coordinates" by Tarun Gehlot][homogeneous-coordinates-video]

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
[wiki-matrix-multi]: https://en.wikipedia.org/wiki/Matrix_multiplication
[wiki-apples-oranges]: https://en.wikipedia.org/wiki/Apples_and_oranges
[khan-defined-vid]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/v/defined-and-undefined-matrix-operations
[khan-defined-course]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/matrix-multiplication-dimensions
[khan-transf-vector]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/v/transforming-position-vector
[khan-transf-polygone]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/v/matrix-transformation-triangle
[khan-matrices-transform]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/a/matrices-as-transformations
[khan-identity-matrix]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/intro-to-identity-matrices
[khan-mult-properties]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/properties-of-matrix-multiplication
[trigonometry]: http://www.mathsisfun.com/algebra/trigonometry.html
[ilaborie]: https://twitter.com/ilaborie
[matrices-texas]: https://www.cs.utexas.edu/~fussell/courses/cs384g/lectures/lecture07-Affine.pdf
[ohio-matrices]: http://web.cse.ohio-state.edu/~whmin/courses/cse5542-2013-spring/6-Transformation_II.pdf
[datagenetics-rotation]: http://datagenetics.com/blog/august32013/
[matrix-rotation-video]: https://www.youtube.com/watch?v=h11ljFJeaLo
[homogeneous-coordinates]: https://en.wikipedia.org/wiki/Homogeneous_coordinates
[homogeneous-coordinates-video]: https://www.youtube.com/watch?v=Xzu8_Fe3ImI
[explaining-homogenous-coordinates]: http://www.tomdalling.com/blog/modern-opengl/explaining-homogenous-coordinates-and-projective-geometry/
[homogeneous-coordinates-blogpost]: http://www.songho.ca/math/homogeneous/homogeneous.html
