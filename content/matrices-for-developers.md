---
title: Matrices for developers
date: 2016-11-19T15:08:29+01:00
description:
tags: ["math","matrix"]
categories: ["math"]
highlight: true
math: true
klipse: true
gallery: true
---

> WARNING: Long article, big images, heavy GIFs.

A few weeks ago I was on an `android-user-group` channel,
when someone posted a question about Android's
[Matrix.postScale(sx, sy, px, py)][Matrix.postScale] method and how it works
because it was *"hard to grasp"*.

Coincidence: in the beginning of 2016, I finished a freelance
project on an [Android application][climbing-away] where I had to implement
an exciting feature:

{{< gallery title="Android app screenshots" >}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-1.png"
  size="1080x1920" caption="Climbing away - App screenshot 1" %}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-2.png"
  size="1080x1920" caption="Climbing away - App screenshot 2" %}}
  {{% galleryimage file="/img/matrices-for-developers/ca-screenshot-3.png"
  size="1080x1920" caption="Climbing away - App screenshot 3" %}}
{{< /gallery >}}

The user, after buying and downloading a digital topography of a crag, had to
be able to view the crag which was composed of:

* a picture of the cliff,
* a SVG file containing an overlay of the climbing routes.

The user had to have the ability to pan and zoom at will and have the routes
layer *"follow"* the picture.

## Technical challenge

In order to have the overlay of routes follow the user's actions, I found I
had to get my hands dirty by overloading an Android `ImageView`, draw onto the
`Canvas` and deal with finger gestures.  
As a good engineer: I searched on Stack Overflow
{{< emoji content=":sweat_smile:" >}}  
And I discovered I'd need the `android.graphics.Matrix` class for 2D
transformations.

The problem with this class, is that it might seem obvious what it does, but if
you have no mathematical background, it's quite mysterious.

> boolean postScale (float sx, float sy, float px, float py)
>
> Postconcats the matrix with the specified scale. M' = S(sx, sy, px, py) * M

Yeah, cool, so it *scales* something with some parameters and it does it with
some kind of multiplication. Nah, I don't get it:

* What does it do exactly? Scales a matrix? What's that supposed to mean, I
  want to scale the canvas...
* What should I use, `preScale` or `postScale`? Do I try both while I get the
  input parameters from my gesture detection code and enter an infinite loop of
  trial and error guesstimates? (No. Fucking. Way.)

So at this very moment of the development process I realized I needed to
re-learn basic math skills about [matrices][wiki-matrices] that I had forgotten
many years ago, after finishing my first two years of uni
{{< emoji content=":scream:" >}}

***WWW to the rescue!***

While searching around I've found a number of good resources and was able to
learn some math again, and it felt great. It also helped me solve my 2D
transformations problems by applying my understandings as code in Java and
Android.

So, given the discussion I've had on the channel I've mentioned above, it
seems I was not the only one struggling with matrices, trying to make sense of
it and using these skills with Android's Matrix class and methods,
so I thought I'd write an article.

The first part, this one, is about matrices. The upcoming second part will be
about how to apply what you know about matrices in code, with Java and
on Android.

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
about a $m \times n$ matrix relates to an array of length $m$ in which
each item is also an array but this time of length $n$. Usually, $m$
represents a rows' number and $n$ a columns' number. Each element in the
matrix is called an *entry*.  
A matrix is represented by a bold capital letter,
and each entry is represented by the same letter, but in lowercase and suffixed
with its row number and column number, in this order. For example:

<div>
$$
\mathbf{A} =
\begin{pmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{pmatrix}
$$
</div>


Now what can we do with it? We can define an algebra for instance: like
[addition, subtraction][khan-addsub-matrices] and
[multiplication][khan-mult-matrices] operations, for fun and profit.
{{< emoji content=":nerd:" >}}

### Addition/Subtraction

[Addition and subtraction of matrices][khan-addsub-matrices] is done by adding
or subtracting the corresponding entries of the operand matrices:

<div>
$$
\mathbf{A} + \mathbf{B} =
\begin{pmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{pmatrix}
+
\begin{pmatrix}
b_{11} & b_{12} & \cdots & b_{1n}\\
b_{21} & b_{22} & \vdots & b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
b_{m1} & b_{m2} & \cdots & b_{mn}
\end{pmatrix}
=
\begin{pmatrix}
a_{11}+b_{11} & a_{12}+b_{12} & \cdots & a_{1n}+b_{1n}\\
a_{21}+b_{21} & a_{22}+b_{22} & \vdots & a_{2n}+b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1}+b_{m1} & a_{m2}+b_{m2} & \cdots & a_{mn}+b_{mn}
\end{pmatrix}
$$
</div>

<div>
$$
\mathbf{A} - \mathbf{B} =
\begin{pmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{pmatrix}
-
\begin{pmatrix}
b_{11} & b_{12} & \cdots & b_{1n}\\
b_{21} & b_{22} & \vdots & b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
b_{m1} & b_{m2} & \cdots & b_{mn}
\end{pmatrix}
=
\begin{pmatrix}
a_{11}-b_{11} & a_{12}-b_{12} & \cdots & a_{1n}-b_{1n}\\
a_{21}-b_{21} & a_{22}-b_{22} & \vdots & a_{2n}-b_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1}-b_{m1} & a_{m2}-b_{m2} & \cdots & a_{mn}-b_{mn}
\end{pmatrix}
$$
</div>

Corollary to this definition we can deduce that in order to be *`defined`*, a matrix
addition or subtraction must be performed against two matrices of same
dimensions $m \times n$, otherwise the *"corresponding entries"* bit would
have no sense:  
Grab a pen and paper and try to add a $3 \times 2$ matrix to a $2 \times 3$
matrix. What will you do with the last *row* of the first matrix? Same question
with the last *column* of the second matrix?  
If you don't know, then you've reached the same conclusion as the mathematicians
that defined matrices additions and subtractions, pretty much
{{< emoji content=":innocent:" >}}

<h4>Examples</h4>

<div class="row row-same-height">
<div class="col-md-6">
$$
\begin{aligned}
\text{Addition}\\
\mathbf{A} + \mathbf{B}
&=
\begin{pmatrix}
4 & -8 & 7\\
0 & 2 & -1\\
15 & 4 & 9
\end{pmatrix}
+
\begin{pmatrix}
-5 & 2 & 3\\
4 & -1 & 6\\
0 & 12 & 3
\end{pmatrix}\\\\
&=
\begin{pmatrix}
4+\left(-5\right) & \left(-8\right)+2 & 7+3\\
0+4               & 2+\left(-1\right) & \left(-1\right)+6\\
15+0              & 4+12              & 9+3
\end{pmatrix}\\\\
\mathbf{A} + \mathbf{B}
&=
\begin{pmatrix}
-1 & -6 & 10\\
4  & 1  & 5\\
15 & 16 & 12
\end{pmatrix}
\end{aligned}
$$
</div>
<div class="col-md-6">
$$
\begin{aligned}
\text{Subtraction}\\
\mathbf{A} - \mathbf{B}
&=
\begin{pmatrix}
4  & -8 & 7\\
0  & 2  & -1\\
15 & 4  & 9
\end{pmatrix}
-
\begin{pmatrix}
-5 & 2 & 3\\
4 & -1 & 6\\
0 & 12 & 3
\end{pmatrix}\\\\
&=
\begin{pmatrix}
4-\left(-5\right) & \left(-8\right)-2 & 7-3\\
0-4               & 2-\left(-1\right) & \left(-1\right)-6\\
15-0              & 4-12              & 9-3
\end{pmatrix}\\\\
\mathbf{A} + \mathbf{B}
&=
\begin{pmatrix}
9  & -10 & 4\\
-4 & 3   & -7\\
15 & -8  & 6
\end{pmatrix}
\end{aligned}
$$
</div>
</div>

<br>

### Matrix multiplication

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
\begin{pmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{pmatrix}
=
\begin{pmatrix}
k.a_{11} & k.a_{12} & \cdots & k.a_{1n}\\
k.a_{21} & k.a_{22} & \vdots & k.a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
k.a_{m1} & k.a_{m2} & \cdots & k.a_{mn}
\end{pmatrix}
$$
</div>

And a little example:

<div>
$$
4 .
\begin{pmatrix}
0  & 3  & 12\\
7  & -5 & 1\\
-8 & 2  & 0
\end{pmatrix}
=
\begin{pmatrix}
0   & 12  & 48\\
28  & -20 & 4\\
-32 & 8   & 0
\end{pmatrix}
$$
</div>

The second type of multiplication operation is the
[multiplication of matrices by matrices][khan-mult-matrices]. This operation
is a little bit more complicated than addition/subtraction because in order
to multiply a matrix by a matrix we don't simply multiply the corresponding
entries. I'll just [quote wikipedia][wiki-matrix-multi] on that one:

> if $\mathbf{A}$ is an $m \times n$ matrix and $\mathbf{B}$ is an
> $n \times p$ matrix, their matrix product $\mathbf{AB}$ is an
> $m \times p$ matrix, in which the $n$ entries across a
> row of $\mathbf{A}$ are multiplied with the $n$ entries down a columns
> of $\mathbf{B}$ and summed to produce an entry of $\mathbf{AB}$

{{< emoji content=":expressionless:" >}}

This hurts my brain, let's break it down:

> if $\mathbf{A}$ is an $m \times n$ matrix and $\mathbf{B}$ is an
> $n \times p$ matrix, their matrix product $\mathbf{AB}$ is an
> $m \times p$ matrix

We can write this in a more graphical way: $
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{A} \\\\ ^{\scriptsize m \times n}\end{matrix} }
\times
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{B} \\\\ ^{\scriptsize n \times p}\end{matrix} } =
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{AB} \\\\ ^{\scriptsize m \times p}\end{matrix} }
$.

See this simple matrix
$
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{A} \\\\ ^{\scriptsize 2 \times 3}\end{matrix} } =
\begin{pmatrix}a\_{11} & a\_{12} & a\_{13}\\\\a\_{21} & a\_{22} & a\_{23}\end{pmatrix}
$
and this other matrix
$
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{B} \\\\ ^{\scriptsize 3 \times 1}\end{matrix} } =
\begin{pmatrix}b\_{11}\\\\b\_{21}\\\\b\_{31}\end{pmatrix}
$.  
We have $m=2$, $n=3$ and $p=1$ so the multiplication will give
$
{\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{AB} \\\\ ^{\scriptsize 2 \times 1}\end{matrix} } =
\begin{pmatrix}ab\_{11}\\\\ab\_{21}\end{pmatrix}
$.

Let's decompose the second part now:

* *"the $n$ entries across a row of $\mathbf{A}$"* means that each row
  in $\mathbf{A}$ is an array of $n=3$ entries: if we take the first row we
  get $a\_{11}$, $a\_{12}$ and $a\_{13}$.
* *"the $n$ entries down a columns of $\mathbf{B}$"* means that each
  column of $\mathbf{B}$ is also an array of $n=3$ entries: in the first
  column we get $b\_{11}$, $b\_{21}$ and $b\_{31}$.
* *"are multiplied with"* means that each entry in $\mathbf{A}$'s row
  must be multiplied with its corresponding (first with first, second with
  second, etc.) entry in $\mathbf{B}$'s column: $a\_{11} \times b\_{11}$,
  $a\_{12} \times b\_{21}$ and $a\_{13} \times b\_{31}$
* *"And summed to produce an entry of $\mathbf{AB}$"* means that we must add
  the products of these corresponding rows and columns entries in order to get
  the entry of the new matrix at this row number and column number: in our case
  we took the products of the entries in the first row in the first matrix with
  the entries in the first column in the second matrix, so this will give us the
  entry in the first row and first column of the new matrix:
  $a\_{11} \times b\_{11} + a\_{12} \times b\_{21} + a\_{13} \times b\_{31}$

To plagiate wikipedia, here is the formula:

<div>
$$
\mathbf{A} =
\begin{pmatrix}
a_{11} & a_{12} & \cdots & a_{1n}\\
a_{21} & a_{22} & \vdots & a_{2n}\\
\vdots & \vdots & \ddots & \vdots\\
a_{m1} & a_{m2} & \cdots & a_{mn}
\end{pmatrix}
\text{, }
\mathbf{B} =
\begin{pmatrix}
b_{11} & b_{12} & \cdots & b_{1p}\\
b_{21} & b_{22} & \vdots & b_{2p}\\
\vdots & \vdots & \ddots & \vdots\\
b_{n1} & b_{n2} & \cdots & b_{np}
\end{pmatrix}
$$
</div>
<div>
$$
\mathbf{AB} =
\begin{pmatrix}
ab_{11} & ab_{12} & \cdots & ab_{1p}\\
ab_{21} & ab_{22} & \vdots & ab_{2p}\\
\vdots  & \vdots  & \ddots & \vdots\\
ab_{m1} & ab_{m2} & \cdots & ab_{mp}
\end{pmatrix}
\text{where }
ab_{ij}=\sum_{k=1}^{m}a_{ik}b_{kj}
$$
</div>

Ok I realize I don't have any better way to explain this so here is a visual
representation of the matrix multiplication process and an example:

<div class="row row-same-height">
<div class="col-md-4">
<a title="By File:Matrix multiplication diagram.svg:User:Bilou See below. [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons" href="https://commons.wikimedia.org/wiki/File%3AMatrix_multiplication_diagram_2.svg"><img width="256" alt="Matrix multiplication diagram 2" src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Matrix_multiplication_diagram_2.svg/256px-Matrix_multiplication_diagram_2.svg.png"/></a>
</div>
<div class="col-md-8">
<div>
$$
\mathbf{A} =
\begin{pmatrix}
4  & 3\\
0  & -5\\
2  & 1\\
-6 & 8
\end{pmatrix}
\text{, }
\mathbf{B} =
\begin{pmatrix}
7  & 1 & 3\\
-2 & 4 & 1
\end{pmatrix}
$$
</div>
<div>
$$
\begin{aligned}
\mathbf{AB}
&=
\begin{pmatrix}
4\times7+3\times\left(-2\right)               & 4\times1+3\times4               & 4\times3+3\times1\\
0\times7+\left(-5\right)\times\left(-2\right) & 0\times1+\left(-5\right)\times4 & 0\times3+\left(-5\right)\times1\\
2\times7+1\times\left(-2\right)               & 2\times1+1\times4               & 2\times3+1\times1\\
\left(-6\right)\times7+8\times\left(-2\right) & \left(-6\right)\times1+8\times4 & \left(-6\right)\times3+8\times1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
28-6   & 4+12  & 12+3\\
0+10   & 0-20  & 0-5\\
14-2   & 2+4   & 6+1\\
-42-16 & -6+32 & -18+8
\end{pmatrix}\\\\
\mathbf{AB}
&=
\begin{pmatrix}
22  & 16  & 15\\
10  & -20 & -5\\
12  & 6   & 7\\
-58 & 26  & -10
\end{pmatrix}
\end{aligned}
$$
</div>
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
\begin{aligned}
2x+y &= 5\\
-x+2y &= 0
\end{aligned}
$$
</div>

Now that you are familiar with matrix multiplications, maybe you can see this
coming, but we can definitely express this system of equations as the following
matrix multiplication:

<div>
$$
\begin{pmatrix}
2  & 1\\
-1 & 2
\end{pmatrix}
.
\begin{pmatrix}
x\\y
\end{pmatrix}
=
\begin{pmatrix}
5\\0
\end{pmatrix}
$$
</div>

If we go a little further, we can see something else based on the matrices
$\begin{pmatrix}x\\\\y\end{pmatrix}$ and
$\begin{pmatrix}5\\\\0\end{pmatrix}$.  
We can see that they can be used to reprensent ***points*** in the Cartesian
plane, right? A point can be represented by a vector originating from origin,
and a vector is just a $2 \times 1$ matrix.

What we have here, is a matrix multiplication that represents the
transformation of a point into another point. We don't know what the first
point's coordinates are yet, and it doesn't matter. What I wanted to show is
that, given a position vector, we are able to *transform* it into another via
a matrix multiplication operation.

Given a point $P$, whose coordinates are represented by the position vector,
$\begin{pmatrix}x\\\\y\end{pmatrix}$, we can obtain a new point $P^{\prime}$
whose coordinates are represented by the position vector
$\begin{pmatrix}x^{\prime}\\\\y^{\prime}\end{pmatrix}$ by multiplying it by a
matrix.

One important thing is that this
*[transformation matrix][khan-transf-vector]* has to have specific
dimensions, in order to fulfill the rule of matrix multiplication: because
$\begin{pmatrix}x\\\\y\end{pmatrix}$ is a $2 \times 1$ matrix, and
$\begin{pmatrix}x^{\prime}\\\\y^{\prime}\end{pmatrix}$ is also a $2 \times 1$
matrix, then the transformation matrix has to be a $2 \times 2$ matrix in
order to have:

<div>
$$
\mathbf{A}
.
\begin{pmatrix}
x\\y
\end{pmatrix}
=
\begin{pmatrix}
a_{11} & a_{12}\\
a_{21} & a_{22}
\end{pmatrix}
.
\begin{pmatrix}
x\\y
\end{pmatrix}
=
\begin{pmatrix}
x^{\prime}\\y^{\prime}
\end{pmatrix}
$$
</div>

> **Note:** The order here is important as we will see later, but you can
> already see that switching $\mathbf{A}$ and
> $\begin{pmatrix}x\\\\y\end{pmatrix}$ would lead to an $undefined$ result
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

Come back here once you've read it, or it's goind to hurt
{{< emoji content=":sweat_smile:" >}}

---

Ok I suppose you've read the course above, but just in case, here
is a reminder

* a position vector $\begin{pmatrix}x\\\\y\end{pmatrix}$ can be broken
  down as
  $\begin{pmatrix}x\\\\y\end{pmatrix} =
  x.\begin{pmatrix}\color{Green} 1\\\\ \color{Green} 0\end{pmatrix}
  +
  y.\begin{pmatrix}\color{Red} 0\\\\ \color{Red} 1\end{pmatrix}$.

{{% fold id="transform-matrix-reminder-1" title="Show explanation" %}}
If you decompose $\begin{pmatrix}x\\\\y\end{pmatrix}$ into a matrix addition
operation, you find
$\begin{pmatrix}x\\\\y\end{pmatrix} =
\begin{pmatrix}x\\\\0\end{pmatrix} + \begin{pmatrix}0\\\\y\end{pmatrix}$.

And if you decompose a little bit more you can express each operand of
this addition as the multiplication of a scalar and a matrix:

* $\begin{pmatrix}x\\\\0\end{pmatrix} = x.\begin{pmatrix}1\\\\0\end{pmatrix}$
* $\begin{pmatrix}0\\\\y\end{pmatrix} = y.\begin{pmatrix}0\\\\1\end{pmatrix}$

Now look at the the matrices $\begin{pmatrix}1\\\\0\end{pmatrix}$ and
$\begin{pmatrix}0\\\\1\end{pmatrix}$, they are the Cartesian unit vectors.

So
$\begin{pmatrix} x\\\\y \end{pmatrix} =
x.\begin{pmatrix}\color{Green} 1\\\\ \color{Green} 0\end{pmatrix}
+
y.\begin{pmatrix}\color{Red} 0\\\\ \color{Red} 1\end{pmatrix}$ is just another
way to write that the position vector $\begin{pmatrix}x\\\\y\end{pmatrix}$
represents a point given by a transformation of the *unit vectors* of our
Cartesian plane.
{{% /fold %}}

* $\begin{pmatrix}\color{Green} a\\\\ \color{Green} c\end{pmatrix}$ and
  $\begin{pmatrix}\color{Red} b\\\\ \color{Red} d\end{pmatrix}$ are the
  position vectors where
  $\begin{pmatrix} \color{Green} 0\\\\ \color{Green} 1\end{pmatrix}$ and
  $\begin{pmatrix} \color{Red} 1\\\\ \color{Red} 0\end{pmatrix}$ will land
  respectively after the transformation matrix
  $\mathbf{A} = \begin{pmatrix} \color{Green} a & \color{Red} b\\\\ \color{Green} c & \color{Red} d \end{pmatrix}$ has been applied.

{{% fold id="transform-matrix-reminder-2" title="Show explanation" %}}
Let's start again from our unit vectors
$\begin{pmatrix} \color{Green} 1\\\\ \color{Green} 0\end{pmatrix}$
and
$\begin{pmatrix} \color{Red} 0\\\\ \color{Red} 1\end{pmatrix}$.

We know that
$\begin{pmatrix} x\\\\y \end{pmatrix} =
x.\begin{pmatrix} \color{Green} 1\\\\ \color{Green} 0\end{pmatrix}
+
y.\begin{pmatrix} \color{Red} 0\\\\ \color{Red} 1\end{pmatrix}$, so now
imagine we apply a transformation to our plane.

Our unit vectors will be transformed too, right?

If we assume that
$\begin{pmatrix} \color{Green} 1\\\\ \color{Green} 0 \end{pmatrix}$
*"lands on"*
$\begin{pmatrix} \color{Green} a\\\\ \color{Green} c \end{pmatrix}$
and that
$\begin{pmatrix} \color{Red} 0\\\\ \color{Red} 1 \end{pmatrix}$
*"lands on"*
$\begin{pmatrix} \color{Red} b\\\\ \color{Red} d \end{pmatrix}$,  
then we have our position vector $\begin{pmatrix} x\\\\y \end{pmatrix}$
landing on
$x.\begin{pmatrix} \color{Green} a\\\\ \color{Green} c \end{pmatrix} +
y.\begin{pmatrix} \color{Red} b\\\\ \color{Red} d \end{pmatrix} =
\begin{pmatrix}\color{Green} a.x + \color{Red} b.y\\\\ \color{Green} c.x + \color{Red} d.y \end{pmatrix}$.
{{% /fold %}}

* given the previous transformation,
  $\begin{pmatrix} x\\\\ y \end{pmatrix}$ will land on
  $\begin{pmatrix} \color{Green} a.x + \color{Red} b.y\\\\ \color{Green} c.x + \color{Red} d.y \end{pmatrix}$.

If you don't understand this conclusion, read again, read the course, take
your time.

----

Now remember, our goal is to determine what $ \mathbf{A} $ is, because we
know the transformation we want to apply but we're searching for the matrix we
should apply to our position vector(s) in order to transform our graphics.

Let's take the example of the transformation of a series of points: we know
where the position vectors will land, but we're looking for $ \mathbf{A} $.  
We have our cartesian plane with a triangle formed by the three points
$P\_{(2,1)}$, $Q\_{(-2,0)}$, $R\_{(0,2)}$, and another triangle which
represents a transformed version of the first one: $P^{\prime}\_{(5, 0)}$
and $Q^{\prime}\_{(-4, 2)}$ and $R^{\prime}\_{(2,4)}$.

{{< gallery title="Example transformation of a triangle" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-transformed-triangle.png"
  size="1215x725" caption="Cartesian plane containing two triangles"
  width="80%" %}}
{{< /gallery >}}

We just need two points for this example, let's take $P$ and $Q$. We know that:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} 2\\ 1 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} 5\\ 0 \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} -2\\ 0 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} -4\\ 2 \end{pmatrix}
    $$</div>
</div>

<br>

Which means:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} x\\ y \end{pmatrix} =
    \begin{pmatrix} 2\\ 1 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} a.x+b.y\\ c.x+d.y \end{pmatrix} =
    \begin{pmatrix} 5\\ 0 \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} x\\ y \end{pmatrix} =
    \begin{pmatrix} -2\\ 0 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} a.x+b.y\\ c.x+d.y \end{pmatrix} =
    \begin{pmatrix} -4\\ 2 \end{pmatrix}
    $$</div>
</div>

<br>

From which we can deduce:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} 2.a+1.b\\ 2.c+1.d \end{pmatrix} =
    \begin{pmatrix} 5\\ 0 \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} -2.a+0.b\\ -2.c+0.d \end{pmatrix} =
    \begin{pmatrix} -4\\ 2 \end{pmatrix}
    $$</div>
</div>

<br>

The right side gives us $ a=2 $ and $ c = -1 $, with which we can deduce
$ b=1 $ and $ d=2 $ from the left side.

**And this, is our transformation matrix**:

<div>$$ \mathbf{A} = \begin{pmatrix} \color{Green} 2 & \color{Red} 1\\ \color{Green} -\color{Green} 1 & \color{Red} 2 \end{pmatrix} $$</div>

Try that same exercise with $P$ and $R$, or with $Q$ and $R$ and you should
end up to the same result.

### The identity matrix

We don't know how to define a transformation matrix yet, but we know its form.  
So what do we do next? Remember the last section where we've seen that
a position vector $\begin{pmatrix} x\\\\ y \end{pmatrix}$ can be
broken down as $\begin{pmatrix} x\\\\y \end{pmatrix} =
x.\begin{pmatrix} \color{Green} 1\\\\ \color{Green} 0 \end{pmatrix} +
y.\begin{pmatrix} \color{Red} 0\\\\ \color{Red} 1 \end{pmatrix} $
?

That's a pretty good starting point, we just laid out our "base" matrix:

<div>
$$
\begin{pmatrix}
\color{Green} 1 & \color{Red} 0\\
\color{Green} 0 & \color{Red} 1
\end{pmatrix}
$$
</div>

This matrix represents the base state of your plane, the matrix applied
to your plane when you have just loaded your image for example (granted
your image is the same size as its receiving container view).  
In other words, ***this is the matrix that, applied to any position vector will
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
  $\left(\mathbf{A}.\mathbf{B}\right).\mathbf{C} = \mathbf{A}.\left(\mathbf{B}.\mathbf{C}\right)$

{{% fold id="matrix-properties-1" title="Show explanation" %}}
Just trust me already!

If you don't, I'm not going to write it here because it takes a lot of screen
width (I've tried and it didn't render very well), so check out this video.

{{< youtube 8Ryfe82DTcM >}}
{{% /fold %}}

* Matrix multiplication is non-commutative
  $\mathbf{A}.\mathbf{B} \neq \mathbf{B}.\mathbf{A}$

{{% fold id="matrix-properties-2" title="Show explanation" %}}
In order to affirm this we just have to prove commutativity wrong, which is
easy!

Imagine $\mathbf{A}$ is a $5 \times 2$ matrix, and $\mathbf{B}$ is a
$2 \times 3$ matrix:

* $
  {\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{A} \\\\ ^{\scriptsize 5 \times 2}\end{matrix} }
  \times
  {\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{B} \\\\ ^{\scriptsize 2 \times 3}\end{matrix} } =
  {\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{AB} \\\\ ^{\scriptsize 5 \times 3}\end{matrix} }
  $
* $
  {\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{B} \\\\ ^{\scriptsize 2 \times 3}\end{matrix} }
  \times
  {\tiny\begin{matrix}^{\scriptsize }\\\\ \normalsize \mathbf{A} \\\\ ^{\scriptsize 5 \times 2}\end{matrix} } =
  undefined
  $

And that's it. But we can also see commutativity does not hold even for
matrices of same dimensions:

<div class="row row-same-height">
  <div class="col-md-6">$$
  \begin{aligned}
  \mathbf{A}.\mathbf{B} &=
  \begin{pmatrix}
  a_{11} & a_{12}\\
  a_{21} & a_{22}
  \end{pmatrix} .
  \begin{pmatrix}
  b_{11} & b_{12}\\
  b_{21} & b_{22}
  \end{pmatrix}\\\\
  &=
  \begin{pmatrix}
  a_{11}.b_{11}+a_{12}.b_{21} & a_{11}.b_{12}+a_{12}.b_{22}\\
  a_{21}.b_{11}+a_{22}.b_{21} & a_{11}.b_{22}+a_{22}.b_{22}
  \end{pmatrix}
  \end{aligned}
  $$</div>
  <div class="col-md-6">$$
  \begin{aligned}
  \mathbf{B}.\mathbf{A} &=
  \begin{pmatrix}
  b_{11} & b_{12}\\
  b_{21} & b_{22}
  \end{pmatrix} .
  \begin{pmatrix}
  a_{11} & a_{12}\\
  a_{21} & a_{22}
  \end{pmatrix}\\\\
  &=
  \begin{pmatrix}
  b_{11}.a_{11}+b_{12}.a_{21} & b_{11}.a_{12}+b_{12}.a_{22}\\
  b_{21}.a_{11}+b_{22}.a_{21} & b_{21}.a_{12}+b_{22}.a_{22}
  \end{pmatrix}
  \end{aligned}
  $$</div>
</div>

Grab a pen and paper and try it for yourself with the following matrices
$\mathbf{A}=\begin{pmatrix}1 & 2\\\\-3 & -4\end{pmatrix}$ and
$\mathbf{B}=\begin{pmatrix}-2 & 0\\\\0 & -3\end{pmatrix}$.
{{% /fold %}}

Back to our transformations.  
Imagine we want to apply transformation $ \mathbf{B} $, then transformation
$ \mathbf{A} $ to our position vector $ \vec{v} $.

We have
$
\vec{v^{\prime}} = \mathbf{B} . \vec{v}
$
and
$
\vec{v^{\prime\prime}} = \mathbf{A} . \vec{v^{\prime}}
$,
which leads us to:

<div>
$$
\vec{v^{\prime\prime}} = \mathbf{A} . \left( \mathbf{B} . \vec{v} \right)
$$
</div>

We know that matrix multiplication is `associative`, which gives us:


<div>
$$
\vec{v^{\prime\prime}} = \mathbf{A} . \left( \mathbf{B} . \vec{v} \right)
\Leftrightarrow
\vec{v^{\prime\prime}} = \left( \mathbf{A} . \mathbf{B} \right) . \vec{v}
$$
</div>

In conclusion, in order to apply multiple transformations at once, we can
multiply all our transformation matrices and apply the resulting transformation
matrix to our vector(s).

We also know that matrix multiplication is `not commutative`, so the order
in which we multiply our transformation matrices
($ \mathbf{A} . \mathbf{B} $ or $ \mathbf{B} . \mathbf{A} $) will have
an impact on our final matrix and will lead to different results, different
transformations.

## Types of transformations

There are several types of 2D transformations we are able to define using
$2 \times 2$ dimensions matrices, and you've had a preview of most of them
in this course on [matrices as transformations][khan-matrices-transform].  
Namely:

* Scaling
* Reflexion
* Shearing
* Rotation

For the rest of this section imagine we have the point
$ P\_{\left(x, y\right)} $, which represents any point of
an object on the plane, and we want to find the matrix to transform it into
$ P^{\prime}\_{\left(x^{\prime}, y^{\prime}\right)}$ such that

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime} \end{pmatrix} =
\mathbf{A} . \begin{pmatrix} x\\y \end{pmatrix} =
\begin{pmatrix} a & b\\c & d \end{pmatrix}
.
\begin{pmatrix} x\\y \end{pmatrix}
$$
</div>

### Scaling

Scaling (like zooming in by a factor of 2 for instance) might seem
straightforward to represent, right?
*"Simply multiply the coordinates by the scaling factor and you're done."*  
But the pitfall here is that you *might* want to have different horizontal and
vertical scaling factors for your transformation, I mean it's possible!

So we must differentiate between $ s\_{x} $ and $ s\_{y} $ which represent
the horizontal and vertical scaling factors, respectively.

The two equations this gives us are:

<div>
$$
\begin{aligned}
x' &= s_{x} . x \\
y' &= s_{y} . y
\end{aligned}
$$
</div>

Knowing that:

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime} \end{pmatrix} =
\begin{pmatrix} a & b\\c & d \end{pmatrix}
.
\begin{pmatrix} x\\y \end{pmatrix}
$$
</div>

We can find $a$, $b$, $c$ and $d$:

<div class="row row-same-height">
    <div class="col-md-6">
$$
\begin{aligned}
s_{x} . x &= a . x + b . y\\\\
\Rightarrow
a &= s_{x} \text{ and }\\
b &= 0
\end{aligned}
$$
</div>
    <div class="col-md-6">
$$
\begin{aligned}
s_{y} . y &= c . x + d . y\\\\
\Rightarrow
c &= s_{y} \text{ and }\\
d &= 0
\end{aligned}
$$
</div>
</div>

<br>

In conclusion, the $2 \times 2$ scaling matrix for the factors
$\left(s\_{x}, s\_{y}\right)$ is:

<div>
$$
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix} s_{x} & 0\\0 & s_{y} \end{pmatrix}
$$
</div>

Which makes sense, right? I mean, scaling by a factor of $1$ both on the
$x$ and $y$ axises will give:

<div>
$$
\begin{pmatrix} s_{x} & 0\\0 & s_{y} \end{pmatrix}
=
\begin{pmatrix} 1 & 0\\0 & 1 \end{pmatrix}
$$
</div>

Which is... the `identity` matrix! So nothing moves, basically.

### Reflexion

There are 2 types of reflexions we can think about right ahead: reflexion around
an axis, or around a point.  
To keep things simple we'll focus on reflexions around the $x$ and $y$
axises (reflexion around the origin is the equivalent of applying a reflexion on
the $x$ and $y$ axises successively).

Reflexion around the $x$ axis gives us:

<div class="row row-same-height">
    <div class="col-md-6">
$$
\begin{aligned}
x^{\prime} &= x\\
x &= a . x + b . y\\\\
\Rightarrow
a &= 1 \text{ and }\\
b &= 0
\end{aligned}
$$
</div>
    <div class="col-md-6">
$$
\begin{aligned}
y^{\prime} &= -y\\
-y &= c . x + d . y\\\\
\Rightarrow
c &= 0 \text{ and }\\
d &= -1
\end{aligned}
$$
</div>
</div>

<br>

Funny, reflecting around the $x$ axis is the same transformation as scaling
$x$ by a factor of $-1$ and $y$ by a factor of $1$:

<div>
$$
\begin{pmatrix} a & b\\c & d \end{pmatrix} =
\begin{pmatrix}
1 & 0\\
0 & -1
\end{pmatrix}
$$
</div>

And reflexion around the $y$ axis gives us:

<div class="row row-same-height">
    <div class="col-md-6">
$$
\begin{aligned}
x^{\prime} &= -x\\
-x &= a . x + b . y\\\\
\Rightarrow
a &= -1 \text{ and }\\
b &= 0
\end{aligned}
$$
</div>
    <div class="col-md-6">
$$
\begin{aligned}
y^{\prime} &= y\\
y &= c . x + d . y\\\\
\Rightarrow
c &= 0 \text{ and }\\
d &= 1
\end{aligned}
$$
</div>
</div>

<br>

The transformation matrix to reflect around the $y$ axis is:

<div>
$$
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix}
-1 & 0\\
0 & 1
\end{pmatrix}
$$
</div>

### Shearing

Now it gets a little bit trickier.

In most examples I've found, shearing is explained by saying the coordinates
are changed by adding a constant that measures the degree of shearing.  
For instance, a shear along the $x$ axis is often represented showing a
rectangle with a vertex at $\left(0, 1\right)$ is transformed into a
parallelogram with a vertex at $\left(1, 1\right)$.

{{< gallery title="$\underline{\text{Shearing along } x \text{ axis by a constant } k_{x}=1}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-constant-shearing.png"
  size="1084x769" caption="Cartesian plane shearing"
  width="80%" %}}
{{< /gallery >}}

In this article, I want to explain it using the shearing angle, the angle
through which the axis is sheared. Let's call it $\alpha$ (alpha).

{{< gallery title="$\underline{\text{Shearing along } x \text{ axis by an angle } \alpha}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-angle-shearing.gif"
  size="1083x691" caption="Cartesian plane shearing"
  width="80%" %}}
{{< /gallery >}}

If we look at the plane above, we can see that the new abscissa $x^{\prime}$ is
equal to $x$ plus/minus the opposite side of the triangle formed
by the $y$ axis, the sheared version of the $y$ axis and the segment
between the top left vertex of the rectangle and the top left vertex of the
parallelogram. In other words, $x^{\prime}$ is equal to $x$ plus/minus the
opposite side of the green triangle, see:

{{< gallery title="$\underline{\text{Triangles formed by shearing along } x \text{ axis by an angle } \alpha}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-angle-shearing-triangles-1.png"
    size="1434x882" title="Shearing by negative alpha=-30 degrees when y(P)>0"
    caption="Shearing by negative $\alpha=-30^{\circ}$ when $y\left(P\right)>0$"
    width="395px" %}}
  {{% galleryimage file="/img/matrices-for-developers/2D-angle-shearing-triangles-2.png"
    size="1434x882" title="Shearing by positive alpha=30 degrees when y(P)>0"
    caption="Shearing by positive $\alpha=30^{\circ}$ when $y\left(P\right)>0$"
    width="395px" %}}
  {{% galleryimage file="/img/matrices-for-developers/2D-angle-shearing-triangles-3.png"
    size="1434x882" title="Shearing by negative alpha=-30 degrees when y(P)<0"
    caption="Shearing by negative $\alpha=-30^{\circ}$ when $y\left(P\right)<0$"
    width="395px" %}}
  {{% galleryimage file="/img/matrices-for-developers/2D-angle-shearing-triangles-4.png"
    size="1434x882" title="Shearing by positive alpha=30 degrees when y(P)<0"
    caption="Shearing by positive $\alpha=30^{\circ}$ when $y\left(P\right)<0$"
    width="395px" %}}
{{< /gallery >}}

Remember your [trigonometry][trigonometry] class?

> In a right-angled triangle:
>
> * the hypotenuse is the longest side
> * the opposite side is the one at the opposite of a given angle
> * the adjacent side is the next to a given angle

* $PP^{\prime}$ is the opposite side, we need to find its length ($k$), in
  order to calculate $x^{\prime}$ from $x$
* the adjacent side is $P$'s ordinate: $y$
* we don't know the hypotenuse's length

From our trigonometry class, we know that:

<div>
$$
\begin{aligned}
\cos \left( \alpha \right) &= \frac{adjacent}{hypotenuse}\\\\
\sin \left( \alpha \right) &= \frac{opposite}{hypotenuse}\\\\
\tan \left( \alpha \right) &= \frac{opposite}{adjacent}
\end{aligned}
$$
</div>

We know $\alpha$, but we don't know the length of the hypotenuse, so we
can't use the cosine function.  
On the other hand, we know the adjacent side's length: it's $y$, so we can use
the tangent function to find the opposite side's length:

<div>
$$
\begin{aligned}
\tan \left( \alpha \right) &= \frac{opposite}{adjacent}\\\\
opposite &= adjacent \times \tan \left( \alpha \right)
\end{aligned}
$$
</div>

We can start solving our system of equations in order to find
our matrix with the following:

<div class="row row-same-height">
    <div class="col-md-6">
$$
x^{\prime} = x + k = x + y . \tan \left( \alpha \right)
$$
</div>
    <div class="col-md-6">
$$
y^{\prime} = y
$$
</div>
</div>

However, we can see that when $\alpha > 0$, $\tan \left( \alpha \right) < 0$ and
when $\alpha < 0$, $\tan \left( \alpha \right) > 0$. This multiplied by $y$
which can itself be positive or negative will give very different results
for $x^{\prime} = x + k = x + y . \tan \left( \alpha \right)$.  
So don't forget that $\alpha > 0$ is counterclockwise rotation/shearing angle,
while $\alpha < 0$ is clockwise rotation/shearing angle.

<div class="row row-same-height">
    <div class="col-md-6">
$$
\begin{aligned}
x^{\prime} &= x + y . \tan \left( \alpha \right) \\
x + y . \tan \left( \alpha \right) &= a . x + b . y\\\\
\Rightarrow
a &= 1 \text{ and }\\
b &= \tan \left( \alpha \right)
\end{aligned}
$$
</div>
    <div class="col-md-6">
$$
\begin{aligned}
y^{\prime} &= y\\
y &= c . x + d . y\\\\
\Rightarrow
c &= 0 \text{ and }\\
d &= 1
\end{aligned}
$$
</div>
</div>

The transformation matrix to shear along the $x$ direction is:

<div>
$$
\begin{aligned}
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix}
1 & \tan \alpha \\
0 & 1
\end{pmatrix}
=
\begin{pmatrix}
1 & k_{x}\\
0 & 1
\end{pmatrix}\\\\
\text{where } k_{x} \text{ is the shearing constant}
\end{aligned}
$$
</div>

Similarly, the transformation matrix to shear along the $y$ direction is:

<div>
$$
\begin{aligned}
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix}
1 & 0\\
\tan \beta & 1
\end{pmatrix}
=
\begin{pmatrix}
1 & 0\\
k_{y} & 1
\end{pmatrix}\\\\
\text{where } k_{y} \text{ is the shearing constant}
\end{aligned}
$$
</div>

### Rotation

Rotations are yet a little bit more complex.

Let's take a closer look at it with an example of rotating (around the origin)
from a angle $ \theta $ (theta).

{{< gallery title="$\underline{\text{Rotate by an angle } \theta}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-rotation.gif"
  size="1083x740" caption="Cartesian plane rotation"
  width="80%" %}}
{{< /gallery >}}

Notice how the coordinates of $P$ and $P^{\prime}$ are the same in their
respective planes:
$P$ and $P^{\prime}$ have the same set of coordinates $ \left( x, y\right) $
in each planes.  
But $P^{\prime}$ has ***new coordinates***
$ \left( x^{\prime}, y^{\prime}\right) $ ***in the first plane***, the one that
has not been rotated.

We can now define the ***relationship*** between the
coordinates $ \left(x, y\right) $ and the new coordinates
$ \left(x^{\prime}, y^{\prime}\right) $, right?

This is where [trigonometry][trigonometry] helps again.

While searching for the demonstration of this, I stumbled upon this
geometry based explanation
by [Nick Berry][datagenetics-rotation] and [this video][matrix-rotation-video].

To be honest, I'm not 100% comfortable with this solution, which means I didn't
fully understand it. And also after re-reading what I've written, Hadrien
(one of the reviewers) and I have found **my** explanation to be a bit
*awkward*.  
So I'm leaving it here in case you're interested, but I suggest you
don't bother unless you're very curious and don't mind a little confusion.

{{% fold id="rotation-matrix-explanation" title="Show fuzzy explanation" %}}

{{< gallery title="$\underline{\text{Trigonometry triangles based on } \theta}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-rotation.png"
  size="1434x980" caption="Trigonometry triangles based on $\theta$"
  width="80%" %}}
{{< /gallery >}}

On this plane we see that $x$ (the blue line) can be expressed as the
**addition** of the
<span class="green-text">adjacent side of the green triangle</span>
**plus**
<span class="red-text">the opposite side of the red triangle.</span>  
And $y$ as the **subtraction** of
<span class="green-text">the opposite side of the green triangle</span>
**from**
<span class="red-text">the adjacent side of the red triangle</span>.  
We know that:

<div>
$$
\begin{aligned}
\cos \left( \theta \right) &= \frac{adjacent}{hypotenuse} \Rightarrow adjacent = hypotenuse \times \cos \left( \theta \right)\\\\
\sin \left( \theta \right) &= \frac{opposite}{hypotenuse} \Rightarrow opposite = hypotenuse \times \sin \left( \theta \right)
\end{aligned}
$$
</div>

So we can express our relationship as follows:

<div class="row row-same-height">
<div class="col-md-6">
$$
\begin{aligned}
x & = \color{Green}a\color{Green}d\color{Green}j\color{Green}a\color{Green}c\color{Green}e\color{Green}n\color{Green}t + \color{Red}o\color{Red}p\color{Red}p\color{Red}o\color{Red}s\color{Red}i\color{Red}t\color{Red}e\\
& = \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \cos \left( \theta \right) + \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \sin \left( \theta \right)\\
& = x^{\prime} . \cos \left( \theta \right) + y^{\prime} . \sin \left( \theta \right)
\end{aligned}
$$
</div>
<div class="col-md-6">
$$
\begin{aligned}
y & = \color{Red}a\color{Red}d\color{Red}j\color{Red}a\color{Red}c\color{Red}e\color{Red}n\color{Red}t - \color{Green}o\color{Green}p\color{Green}p\color{Green}o\color{Green}s\color{Green}i\color{Green}t\color{Green}e\\
& = \color{Red}h\color{Red}y\color{Red}p\color{Red}o\color{Red}t\color{Red}e\color{Red}n\color{Red}u\color{Red}s\color{Red}e . \cos \left( \theta \right) - \color{Green}h\color{Green}y\color{Green}p\color{Green}o\color{Green}t\color{Green}e\color{Green}n\color{Green}u\color{Green}s\color{Green}e . \sin \left( \theta \right)\\
& = y^{\prime} . \cos \left( \theta \right) - x^{\prime} . \sin \left( \theta \right)\\
& = -x^{\prime} . \sin \left( \theta \right) + y^{\prime} . \cos \left( \theta \right)
\end{aligned}
$$
</div>
</div>

In the end what we really have here is a system of equations that we can
represent as a $2 \times 2$ matrix:

<div>
$$
\begin{pmatrix}
x\\
y
\end{pmatrix}
=
\begin{pmatrix}
\cos \theta & \sin \theta\\
-\sin \theta & \cos \theta
\end{pmatrix}
.
\begin{pmatrix}
x^{\prime}\\
y^{\prime}
\end{pmatrix}
$$
</div>

But this is not *exactly* what we are looking for, right?  
This defines the relationship to convert from the *new* coordinates in the
original plane
$ \left(x^{\prime}, y^{\prime}\right) $
what are the coordinates $ \left(x, y\right) $ in the rotated
plane.  
Whereas what we want to define is how to convert from the rotated plane
(the coordinates that we know) to the original plane.

In order to do what we want, we need to take the same matrix, but define a
rotation of $ - \theta $.

We know that:

<div>
$$
\begin{aligned}
\cos \left( -\theta \right) &= cos \left( \theta \right)\\
\sin \left( -\theta \right) &= - sin \left( \theta \right)
\end{aligned}
$$
</div>

Which gives us our desired rotation matrix:

<div>
$$
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix}
\cos \theta & -\sin \theta\\
\sin \theta & \cos \theta
\end{pmatrix}
$$
</div>

{{% /fold %}}

Now for the simple demonstration I'm going to go with the *"This position
vector *lands on* this position vector"* route.

Suppose you are zooming on the unit vectors like so:

{{< gallery title="Trigonometry triangles based on $\underline{\theta}$" >}}
  {{% galleryimage file="/img/matrices-for-developers/2D-rotation-unit.png"
  size="1494x1020" caption="Unit vectors under rotation by $\underline{\theta}$"
  width="80%" %}}
{{< /gallery >}}

Based on the rules of trigonometry we've already seen, we have:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} 0\\ 1 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} \cos \theta \\ \sin \theta \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} 1\\ 0 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} - \sin \theta \\ \cos \theta \end{pmatrix}
    $$</div>
</div>

<br>

Which means:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} x\\ y \end{pmatrix} =
    \begin{pmatrix} 1\\ 0 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} a.x+b.y\\ c.x+d.y \end{pmatrix} =
    \begin{pmatrix} \cos \theta \\ \sin \theta \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} x\\ y \end{pmatrix} =
    \begin{pmatrix} 0\\ 1 \end{pmatrix}
    \text{ lands on }
    \begin{pmatrix} a.x+b.y\\ c.x+d.y \end{pmatrix} =
    \begin{pmatrix} - \sin \theta \\ \cos \theta \end{pmatrix}
    $$</div>
</div>

<br>

From which we can deduce:

<div class="row row-same-height">
    <div class="col-md-6">$$
    \begin{pmatrix} 1.a+0.b\\ 1.c+0.d \end{pmatrix} =
    \begin{pmatrix} \cos \theta \\ \sin \theta \end{pmatrix}
    $$</div>
    <div class="col-md-6">$$
    \begin{pmatrix} 0.a+1.b\\ 0.c+1.d \end{pmatrix} =
    \begin{pmatrix} - \sin \theta \\ \cos \theta \end{pmatrix}
    $$</div>
</div>

<br>

Easy to deduce $ a = \cos \left( \theta \right) $,
$ b = - \sin \left( \theta \right) $,
$ c = \sin \left( \theta \right) $ and $ d = \cos \left( \theta \right) $.

Which gives us our desired rotation matrix:

<div>
$$
\begin{pmatrix} a & b\\c & d \end{pmatrix}
=
\begin{pmatrix}
\cos \theta & -\sin \theta\\
\sin \theta & \cos \theta
\end{pmatrix}
$$
</div>

Congratulations! You know of to define scaling, reflexion, shearing and rotation
transformation matrices. So what is missing?

## 3x3 transformation matrices

If you're still with me at this point, maybe you're wondering why any of this
is useful. If it's the case, you missed the point of this article, which is to
***understand*** affine transformations in order to apply them in code
{{% emoji content=":mortar_board:" %}}.

This is useful because at this point you know what a transformation matrix
looks like, and you know how to compute one given a few position vectors,
and it is also a great accomplishment by itself.

But here's the thing: $2 \times 2$ matrices are limited in the number of
operations we can perform. With a $2 \times 2$ matrix, the only transformations
we can do are the ones we've seen in the previous section:

* Scaling
* Reflexion
* Shearing
* Rotation

So what are we missing? Answer: **translations**!  
And this is unfortunate, as translations are really useful, like when the user
pans and the image has to behave accordingly (aka. *follow the finger*).  
Translations are defined by the addition of two matrices :

<div>
$$
\begin{pmatrix}
x'\\
y'
\end{pmatrix}
=
\begin{pmatrix}
x\\
y
\end{pmatrix}
+
\begin{pmatrix}
t_{x}\\
t_{y}
\end{pmatrix}
$$
</div>

But we want our user to be able to combine/chain transformations (like
zooming on a specific point which is not the origin), so we need to find a
way to express translations as matrices multiplications too.

Here comes the world of [Homogeneous coordinates][homogeneous-coordinates]...

**No, you don't *have* to read it**, and no I don't totally get it either...

The gist of it is:

* the Cartesian plane you're used to, is really just one of many
  planes that exist in the 3D space, and is at $ z = 1 $
* for any point $ \left(x, y, z\right)$ in the 3D space, the line in
  the projecting space that is going through this point and the origin is
  also passing through any point that is obtained by scaling
  $x$, $y$ and $z$ by the same factor
* the coordinates of any of these points on the line is
  $ \left(\frac{x}{z}, \frac{y}{z}, z\right)$.

{{< gallery title="Homogeneous coordinates graphics" >}}
  {{% galleryimage file="/img/matrices-for-developers/homogeneous-coordinates.png"
  size="1433x1346" caption="Homogeneous coordinates"
  width="80%" %}}
{{< /gallery >}}

I've collected a list of blog posts, articles and videos links at the end of
this post if you're interested.

Without further dig in, this is helping, because it says that we
can now represent any point in our Cartesian plane ($ z = 1 $) not only as
a $2 \times 1$ matrix, but also as a $3 \times 1$ matrix:

<div>
$$
\begin{pmatrix}
x\\
y
\end{pmatrix}
\Leftrightarrow
\begin{pmatrix}
x\\
y\\
1
\end{pmatrix}
$$
</div>

Which means we have to redefine all our previous transformation matrices,
because the product of a $3 \times 1$ matrix (position vector) by a
$2 \times 2$ matrix (transformation) is *`undefined`*.

**Don't rage quit! It's straightforward: $\mathbf{z^{\prime}=z}$!**

We have to find the transformation matrix $
\mathbf{A} =
\begin{pmatrix} a & b & c\\\\ d & e & f\\\\ g & h & i \end{pmatrix}
$

If, like in the previous section, we imagine that we have the point
$ P_{\left(x, y, z\right)} $, which represents any point of
an object on the cartesian plane, then we want to find the matrix to transform
it into $ P^{\prime}\_{\left(x^{\prime}, y^{\prime}, z^{\prime}\right)}$ such that

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime}\\z^{\prime} \end{pmatrix} =
\mathbf{A} . \begin{pmatrix} x\\y\\z \end{pmatrix} =
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
.
\begin{pmatrix} x\\y\\z \end{pmatrix}
$$
</div>


### Scaling

We are looking for $\mathbf{A}$ such that:

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime}\\z^{\prime} \end{pmatrix} =
\begin{pmatrix} s_{x}.x\\s_{y}.y\\z \end{pmatrix} =
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
.
\begin{pmatrix} x\\y\\z \end{pmatrix}
$$
</div>

We can solve the following system of equation in order to find $\mathbf{A}$:

<div class="row row-same-height">
    <div class="col-md-4">
$$
\begin{aligned}
x^{\prime} &= s_{x} . x\\
s_{x} . x &= a . x + b . y + c . z\\\\
\Rightarrow
a &= s_{x} \text{ and }\\
b &= 0 \text{ and }\\
c &= 0
\end{aligned}
$$
    </div>
    <div class="col-md-4">
$$
\begin{aligned}
y^{\prime} &= s_{y} . y\\
s_{y} . y &= d . x + e . y + f + z\\\\
\Rightarrow
d &= s_{y} \text{ and }\\
e &= 0 \text{ and }\\
f &= 0
\end{aligned}
$$
    </div>
    <div class="col-md-4">
$$
\begin{aligned}
z^{\prime} &= z\\
\Rightarrow
z &= g . x + h . y + i + z\\
\Rightarrow
g &= 0 \text{ and }\\
h &= 0 \text{ and }\\
i &= 1
\end{aligned}
$$
    </div>
</div>

<br>

The 3x3 scaling matrix for the factors
$ \left(s\_{x}, s\_{y}\right) $ is:

<div>
$$
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
=
\begin{pmatrix} s_{x} & 0 &0\\0 & s_{y} & 0\\0 & 0 & 1\end{pmatrix}
$$
</div>

### Reflexion

For a reflexion around the $x$ axis we are looking for $\mathbf{A}$ such
that:

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime}\\z^{\prime} \end{pmatrix} =
\begin{pmatrix} x\\-y\\z \end{pmatrix} =
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
.
\begin{pmatrix} x\\y\\z \end{pmatrix}
$$
</div>

We can solve the following system of equation in order to find $\mathbf{A}$:

<div class="row row-same-height">
    <div class="col-md-4">
$$
\begin{aligned}
x^{\prime} &= x\\
x &= a . x + b . y + c . z\\\\
\Rightarrow
a &= 1 \text{ and }\\
b &= 0 \text{ and }\\
c &= 0
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
y^{\prime} &= -y\\
-y &= d . x + e . y + f . z\\\\
\Rightarrow
d &= 0 \text{ and }\\
e &= -1 \text{ and }\\
f &= 0
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
z^{\prime} &= z\\
z &= g . x + h . y + i . z\\\\
\Rightarrow
g &= 0 \text{ and }\\
h &= 0 \text{ and }\\
i &= 1
\end{aligned}
$$
</div>
</div>

<br>

The transformation matrix to reflect around the $x$ axis is:

<div>
$$
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
=
\begin{pmatrix}
1 & 0 & 0\\
0 & -1 & 0\\
0 & 0 & 1
\end{pmatrix}
$$
</div>

For the reflexion around the $y$ axis we are looking for $\mathbf{A}$ such
that:

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime}\\z^{\prime} \end{pmatrix} =
\begin{pmatrix} x\\-y\\z \end{pmatrix} =
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
.
\begin{pmatrix} x\\y\\z \end{pmatrix}
$$
</div>

We can solve the following system of equation in order to find $\mathbf{A}$:

<div class="row row-same-height">
    <div class="col-md-4">
$$
\begin{aligned}
x^{\prime} &= -x\\
-x &= a . x + b . y + c . z\\\\
\Rightarrow
a &= -1 \text{ and }\\
b &= 0 \text{ and }\\
c &= 0
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
y^{\prime} &= y\\
y &= d . x + e . y + f . z\\\\
\Rightarrow
d &= 0 \text{ and }\\
e &= 1 \text{ and }\\
f &= 0
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
z^{\prime} &= z\\
z &= g . x + h . y + i . z\\\\
\Rightarrow
g &= 0 \text{ and }\\
h &= 0 \text{ and }\\
i &= 1
\end{aligned}
$$
</div>
</div>

<br>

The transformation matrix to reflect around the $y$ axis is:

<div>
$$
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
=
\begin{pmatrix}
-1 & 0 & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{pmatrix}
$$
</div>

### Shearing

Well, I'm a bit lazy here {{% emoji content=":hugging:" %}}  
You see the pattern, right? Third line always the same, third column always the
same.

The transformation matrix to shear along the $x$ direction is:

<div>
$$
\begin{aligned}
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
&=
\begin{pmatrix}
1 & \tan \alpha & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
1 & k_{x} & 0\\
0 & 1 & 0\\
0 & 0 & 1
\end{pmatrix}\\\\
& \text{where } k \text{ is the shearing constant}
\end{aligned}
$$
</div>

Similarly, the transformation matrix to shear along the $y$ direction is:

<div>
$$
\begin{aligned}
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
&=
\begin{pmatrix}
1 & 0 & 0\\
\tan \beta & 1 & 0\\
0 & 0 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
1 & 0 & 0\\
k_{y} & 1 & 0\\
0 & 0 & 1
\end{pmatrix}\\\\
& \text{where } k \text{ is the shearing constant}
\end{aligned}
$$
</div>

### Rotating

Same pattern, basically we just take the $2 \times 2$ rotation matrix
and add one row and one column whose entries are $0$, $0$ and $1$.

<div>
$$
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
=
\begin{pmatrix}
\cos \theta & -\sin \theta & 0\\
\sin \theta & \cos \theta & 0\\
0 & 0 & 1
\end{pmatrix}
$$
</div>

But you can do the math, if you want
{{% emoji content=":stuck_out_tongue_winking_eye:" %}}

### Translation

And now it gets interesting, because we can define translations as
$3 \times 3$ matrices multiplication!

We are looking for $\mathbf{A}$ such that:

<div>
$$
\begin{pmatrix} x^{\prime}\\y^{\prime}\\z^{\prime} \end{pmatrix} =
\begin{pmatrix} x+t_{x}\\y+t_{y}\\z \end{pmatrix} =
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
.
\begin{pmatrix} x\\y\\z \end{pmatrix}
$$
</div>

We can solve the following system of equation in order to find $\mathbf{A}$:

<div class="row row-same-height">
    <div class="col-md-4">
$$
\begin{aligned}
x^{\prime} &= x + t_{x} \\
x + t_{x} &= a . x + b . y + c . z\\\\
\Rightarrow
a &= 1 \text{ and }\\
b &= 0 \text{ and }\\
c &= t_{x}
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
y^{\prime} &= y + t_{y}\\
y + t_{y} &= d . x + e . y + f . z\\\\
\Rightarrow
d &= 0 \text{ and }\\
e &= 1 \text{ and }\\
f &= t_{y}
\end{aligned}
$$
</div>
    <div class="col-md-4">
$$
\begin{aligned}
z^{\prime} &= z\\
z &= g . x + h . y + i . z\\\\
\Rightarrow
g &= 0 \text{ and }\\
h &= 0 \text{ and }\\
i &= 1
\end{aligned}
$$
</div>
</div>

The $3 \times 3$ translation matrix for the translation
$ \left(t\_{x}, t\_{y}\right) $ is:

<div>
$$
\begin{pmatrix} a & b & c\\d & e & f\\g & h & i\end{pmatrix}
=
\begin{pmatrix} 1 & 0 & t_{x}\\0 & 1 & t_{y}\\0 & 0 & 1\end{pmatrix}
$$
</div>

## Matrices wrap-up

Obviously, you won't have to go into all of these algebra stuff each
time you want to know what is the matrix you need to apply in order to do
your transformations.

You can just use the following:

### Reminder

Translation matrix:
$\begin{pmatrix}1 & 0 & t\_{x}\\\\0 & 1 & t\_{y}\\\\0 & 0 & 1\end{pmatrix}$

Scaling matrix:
$\begin{pmatrix}s\_{x} & 0 & 0\\\\0 & s\_{y} & 0\\\\0 & 0 & 1\end{pmatrix}$

Shear matrix:
$\begin{pmatrix}1 & \tan \alpha & 0\\\\\tan \beta & 1 & 0\\\\0 & 0 & 1\end{pmatrix} =
\begin{pmatrix}1 & k\_{x} & 0\\\\k\_{y} & 1 & 0\\\\0 & 0 & 1\end{pmatrix}$

Rotation matrix:
$\begin{pmatrix}\cos \theta & -\sin \theta & 0\\\\\sin \theta & \cos \theta & 0\\\\0 & 0 & 1\end{pmatrix}$

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
at the center of the square, to mimic a pinch-zoom behaviour
{{% emoji content=":mag:" %}}  
This transformation is composed of the following sequence:

* move anchor point to origin: $ \left( -t\_{x}, -t\_{y} \right) $
* scale by $ \left( s\_{x}, s\_{y} \right) $
* move back anchor point: $ \left( t\_{x}, t\_{y} \right) $

Where $t$ is the anchor point of our scaling transformation (the center of
the square).

Our transformations are defined by the first translation matrix
$ \mathbf{C} $, the scaling matrix $ \mathbf{B} $, and the last
translation matrix $ \mathbf{A} $.

<div>
$$
\mathbf{C} =
\begin{pmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{pmatrix}
\text{ , }
\mathbf{B} =
\begin{pmatrix}
s_{x} & 0 & 0 \\
0 & s_{y} & 0 \\
0 & 0 & 1
\end{pmatrix}
\text{ and }
\mathbf{A} =
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
$$
</div>

Because matrix multiplication is non-commutative, the order matters, so we will
apply them in reverse order (hence the reverse naming order).  
The composition of these transformations gives us the following product:

<div>
$$
\begin{aligned}
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
s_{x} & 0 & 0 \\
0 & s_{y} & 0 \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
s_{x} & 0 & -s_{x}.t_{x} \\
0 & s_{y} & -s_{y}.t_{y} \\
0 & 0 & 1
\end{pmatrix}\\\\
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{pmatrix}
s_{x} & 0 & -s_{x}.t_{x} + t_{x} \\
0 & s_{y} & -s_{y}.t_{y} + t_{y} \\
0 & 0 & 1
\end{pmatrix}
\end{aligned}
$$
</div>

Suppose we have the following points representing a square:
$\begin{pmatrix}x\_{1} & x\_{2} & x\_{3} & x\_{4}\\\\y\_{1} & y\_{2} & y\_{3} & y\_{4}\\\\1 & 1 & 1 & 1\end{pmatrix} =
\begin{pmatrix}2 & 4 & 4 & 2\\\\1 & 1 & 3 & 3\\\\1 & 1 & 1 & 1\end{pmatrix}$

{{< gallery title="Pinch-zoom four points demo" >}}
  {{% galleryimage file="/img/matrices-for-developers/pinch-zoom-init.png"
  size="1032x587" caption="Pinch-zoom four points demo"
  width="80%" %}}
{{< /gallery >}}

And we want to apply a 2x zoom focusing on its center.  
The new coordinates will be:

<div>
$$
\begin{aligned}
\begin{pmatrix}
x_{1}^{\prime} & x_{2}^{\prime} & x_{3}^{\prime} & x_{4}^{\prime}\\
y_{1}^{\prime} & y_{2}^{\prime} & y_{3}^{\prime} & y_{4}^{\prime}\\
1 & 1 & 1 & 1
\end{pmatrix}
&=
\begin{pmatrix}
s_{x} & 0 & -s_{x}.t_{x} + t_{x} \\
0 & s_{y} & -s_{y}.t_{y} + t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
2 & 0 & -2.3 + 3 \\
0 & 2 & -2.2 + 2 \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
2 & 0 & -3 \\
0 & 2 & -2 \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
\begin{pmatrix}
x_{1}^{\prime} & x_{2}^{\prime} & x_{3}^{\prime} & x_{4}^{\prime}\\
y_{1}^{\prime} & y_{2}^{\prime} & y_{3}^{\prime} & y_{4}^{\prime}\\
1 & 1 & 1 & 1
\end{pmatrix}
&=
\begin{pmatrix}
1 & 5 & 5 & 1\\
0 & 0 & 4 & 4\\
1 & 1 & 1 & 1
\end{pmatrix}
\end{aligned}
$$
</div>

{{< gallery title="Pinch-zoom four points demo" >}}
  {{% galleryimage file="/img/matrices-for-developers/pinch-zoom.gif"
  size="1214x691" caption="Pinch-zoom four points demo"
  width="80%" %}}
{{< /gallery >}}

### Combination use-case: rotate image

Now imagine you have an image in a view, the origin is not a the center of the
view, it is probably at the top-left corner (implementations may vary),
but you want to rotate the image at the center of the view
{{% emoji content=":upside_down:" %}}  
This transformation is composed of the following sequence:

* move anchor point to origin: $ \left( -t\_{x}, -t\_{y} \right) $
* rotate by $ \theta $
* move back anchor point: $ \left( t\_{x}, t\_{y} \right) $

Where $t$ is the anchor point of our rotation transformation.

Our transformations are defined by the first translation matrix
$ \mathbf{C} $, the rotation matrix $ \mathbf{B} $, and the last
translation matrix $ \mathbf{A} $.

<div>
$$
\mathbf{C} =
\begin{pmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{pmatrix}
\text{ , }
\mathbf{B} =
\begin{pmatrix}
\cos \theta & -\sin \theta & 0 \\
\sin \theta & \cos \theta & 0 \\
0 & 0 & 1
\end{pmatrix}
\text{ and }
\mathbf{A} =
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
$$
</div>

The composition of these transformations gives us the following product:

<div>
$$
\begin{aligned}
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
\cos \theta & -\sin \theta & 0 \\
\sin \theta & \cos \theta & 0 \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
1 & 0 & -t_{x} \\
0 & 1 & -t_{y} \\
0 & 0 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
1 & 0 & t_{x} \\
0 & 1 & t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
\cos \theta & -\sin \theta & -\cos \theta.t_{x} +\sin \theta.t_{y} \\
\sin \theta & \cos \theta & -\sin \theta.t_{x} -\cos \theta.t_{y} \\
0 & 0 & 1
\end{pmatrix}\\\\
\mathbf{A} . \mathbf{B} . \mathbf{C}
&=
\begin{pmatrix}
\cos \theta & -\sin \theta & -\cos \theta.t_{x} +\sin \theta.t_{y} + t_{x} \\
\sin \theta & \cos \theta & -\sin \theta.t_{x} -\cos \theta.t_{y} + t_{y} \\
0 & 0 & 1
\end{pmatrix}
\end{aligned}
$$
</div>


Suppose we have the following points representing a square:
$\begin{pmatrix}x\_{1} & x\_{2} & x\_{3} & x\_{4}\\\\y\_{1} & y\_{2} & y\_{3} & y\_{4}\\\\1 & 1 & 1 & 1\end{pmatrix} =
\begin{pmatrix}2 & 4 & 4 & 2\\\\1 & 1 & 3 & 3\\\\1 & 1 & 1 & 1\end{pmatrix}$

{{< gallery title="Rotate image (four points demo)" >}}
  {{% galleryimage file="/img/matrices-for-developers/rotate-image-init.png"
  size="1032x587" caption="Rotate image (four points demo)"
  width="80%" %}}
{{< /gallery >}}

And we want to apply a rotation of $ \theta = 90^{\circ} $ focusing on its center.  
The new coordinates will be:

<div>
$$
\begin{aligned}
\begin{pmatrix}
x_{1}^{\prime} & x_{2}^{\prime} & x_{3}^{\prime} & x_{4}^{\prime}\\
y_{1}^{\prime} & y_{2}^{\prime} & y_{3}^{\prime} & y_{4}^{\prime}\\
1 & 1 & 1 & 1
\end{pmatrix}
&=
\begin{pmatrix}
\cos \theta & -\sin \theta & -\cos \theta.t_{x} +\sin \theta.t_{y} + t_{x} \\
\sin \theta & \cos \theta & -\sin \theta.t_{x} -\cos \theta.t_{y} + t_{y} \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
x_{1} & x_{2} & x_{3} & x_{4}\\
y_{1} & y_{2} & y_{3} & y_{4}\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
0 & -1 & -0.3+1.2+3 \\
1 & 0  & -1.3-0.2+2 \\
0 & 0  & 1
\end{pmatrix}
.
\begin{pmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
&=
\begin{pmatrix}
0 & -1 & 5 \\
1 & 0 & -1 \\
0 & 0 & 1
\end{pmatrix}
.
\begin{pmatrix}
2 & 4 & 4 & 2\\
1 & 1 & 3 & 3\\
1 & 1 & 1 & 1
\end{pmatrix}\\\\
\begin{pmatrix}
x_{1}^{\prime} & x_{2}^{\prime} & x_{3}^{\prime} & x_{4}^{\prime}\\
y_{1}^{\prime} & y_{2}^{\prime} & y_{3}^{\prime} & y_{4}^{\prime}\\
1 & 1 & 1 & 1
\end{pmatrix}
&=
\begin{pmatrix}
4 & 4 & 2 & 2\\
1 & 3 & 3 & 1\\
1 & 1 & 1 & 1
\end{pmatrix}
\end{aligned}
$$
</div>

{{< gallery title="Rotate image (four points demo)" >}}
  {{% galleryimage file="/img/matrices-for-developers/rotate-image.gif"
  size="1032x587" caption="Rotate image (four points demo)"
  width="80%" %}}
{{< /gallery >}}

## Acknowledgements

I want to address my warmest thank you to the following people, who helped me
during the review process of this article, by providing helpful feedbacks and
advices:

* Igor Laborie ([@ilaborie][ilaborie])
* Hadrien Toma

## Resources

* [All the Geogebra files I've used to generate the graphics and gifs][github-this]

## Links

* [Khan Academy algebra course on matrices][khan-alg-matrices]
* [A course on "Affine Transformation" at The University of Texas at Austin][matrices-texas]
* [A course on "Composing Transformations" at The Ohio State University][ohio-matrices]
* [A blogpost on "Rotating images" by Nick Berry][datagenetics-rotation]
* [A Youtube video course on "The Rotation Matrix" by Michael J. Ruiz][matrix-rotation-video]
* [Wikipedia on Homogeneous coordinates][homogeneous-coordinates]
* [A blogpost on "Explaining Homogeneous Coordinates & Projective Geometry" by Tom Dalling][explaining-homogenous-coordinates]
* [A blogpost on "Homogeneous Coordinates" by Song Ho Ahn][homogeneous-coordinates-blogpost]
* [A Youtube video course on "2D transformations and homogeneous coordinates" by Tarun Gehlot][homogeneous-coordinates-video]

[github-this]: https://github.com/arnaudbos/i-rant/tree/develop/static/code/matrices-for-developers
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
[matrices-texas]: https://www.cs.utexas.edu/~fussell/courses/cs384g-fall2010/lectures/lecture07-Affine.pdf
[ohio-matrices]: http://web.cse.ohio-state.edu/~whmin/courses/cse5542-2013-spring/6-Transformation_II.pdf
[datagenetics-rotation]: http://datagenetics.com/blog/august32013/
[matrix-rotation-video]: https://www.youtube.com/watch?v=h11ljFJeaLo
[homogeneous-coordinates]: https://en.wikipedia.org/wiki/Homogeneous_coordinates
[homogeneous-coordinates-video]: https://www.youtube.com/watch?v=Xzu8_Fe3ImI
[explaining-homogenous-coordinates]: http://www.tomdalling.com/blog/modern-opengl/explaining-homogenous-coordinates-and-projective-geometry/
[homogeneous-coordinates-blogpost]: http://www.songho.ca/math/homogeneous/homogeneous.html
