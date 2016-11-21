---
title: 2D transformations with Matrices for Java and Android developers
date: 2016-11-19T15:08:29+01:00
description:
tags: ["java","android","matrix"]
categories: ["java","android"]
draft: true
highlight: true
math: true
---

Last week I was on the `android-user-group` channel of our local techies Slack
channel, when someone posted a question about Android's
[Matrix.postScale(sx, sy, px, py)][Matrix.postScale] method and how it works
because it was `"hard to grasp"`.

Coincidence: a few months ago, at the beginning of 2016, I finished a freelance
project on an [Android application][climbing-away] where I had to implement
an exciting feature:

The user, after buying and downloading a digital topography of a crag, had
be able to view the crag which was composed of:

* an image of the cliff, and
* a SVG file containing the overlay of the climbing routes.

Now the user had to have the ability to pan and zoom at will on the crag (image)
and have the routes layer "*follow*" the image.

In order to do this, the SVG parser had to build a tree of objects representing
SVG DOM elements, down to the `path`, `line`, `polyline`, `polygon`, `rect`,
`circle` and `ellipse` elements, respectively represented in Android classes as
`android.graphics` classes: `Path`, `Path`, `Path`, `Path`, `RectF`, `RectF` and
`RectF`.

Nothing fancy here (except the parsing part), the first four can be drawn using
a `Path` only, and the remaining three are just special drawing methods based on
the `RectF` specification.

I will not enter into the performance optimization twists I (modestly) made
here, but feel free to contact me to discuss the matter and I'll write a
follow-up post on the subject.

## Now the interesting stuff

Now what was interesting, was the pan and zooming stuff, because in order to
have the overlay of routes to follow the user's actions, I had to apply 2D
affine transformations, aka applying matrices.

Actually at this very moment of the development process I realized I'd need
basic math skills about [matrices][matrix] that I've forgotten many years prior
after finishing my first two years of uni 😱

***Google to the rescue!***... well, the World-Wide Web to the rescue actually.

While searching around I've found a number of good resources and was able to
learn some math again, and it felt great. It also helped me solve my 2D
transformations problems by applying my understandings in code, as Java and
Android Matrix operations.

Now given the discussion I've had on the Slack channel I've mentioned above, it
seems I am not the only one struggling with Android's Matrix class and methods,
so I thought I'd write an article.

## Talk about Matrices goddammit!

Alright, alright.

The first resource you might encounter when trying to understand affine
transformations is Wikipedia:

* https://en.wikipedia.org/wiki/Transformation_matrix#Affine_transformations
* https://en.wikipedia.org/wiki/Affine_transformation

Well with this I almost got—  

wait...

{{< img src="/img/matrices-java-and-android/awkward-seal.png"
alt="Awkward seal" width="100%">}}

NOPE, nevermind, I didn't get anything at all.

But there's hope, on *Khan Academy* you will find a very well taught [algebra
course about matrices][khan-alg-matrices].

If you have this kind of problem, I encourage you to take the time needed to
follow this course until you reach that "AHA" moment, it's just a few hours of
investment (because it's free) and you won't regret it. On the other hand I will
point you to the parts in particular that helped me.

### First things firsts: What is a matrix?

Well, you'll find out by watching this video and [the accompanying course here]
[khan-intro-matrices].

{{< youtube 0oGJTQCy4cQ >}}

<br>

So a matrix is an array of numbers, fantastic. Now what can we do with it?

Well, we can now define an algebra for it, like [addition, subtraction]
[khan-addsub-matrices] and multiplication operations, for fun and profit.

**Spoiler alert**: for 2D transformations you'll be more interested in
multiplication than addition/subtraction, but I really encourage you to follow
the whole course, it's worth it.

### Matrix multiplication

Still reading? Okay, let's multiply:

{{< youtube kT4Mp9EdVqs >}}

<br>

And [here is the accompanying course][khan-mult-matrices] to have a better
understanding of what's going on, and practice a bit.

Now the bad news: ***you can't always multiply two matrices***.

Just as there's a rule for adding/subtracting two matrices (the two matrices
must have the same dimensions), there's a rule for multiplying two matrices:

> In order for matrix multiplication to be defined, the number of columns in
> the first matrix must be equal to the number of rows in the second matrix.

Otherwise you just **can't** multiply, period.

More details [here][khan-defined-vid] and [here][khan-defined-course] if you
are interested.

### Intro to Matrix transformation

Now that we know what is a matrix and how we can multiply matrices, we can see
why it is interesting for **2D transformations**.

The next video will show you how you can use a matrix to **transform a point**!

{{< youtube Kh8HKAxdEyw >}}

<br>

Or **a series of points**!

{{< youtube RKBSX-6pKgY >}}

<br>

Notice the nature of the transformation is not clear yet, the transformation
matrix was picked at random, and yet we see how interesting and useful it is
for 2D manipulation of graphics.

For know I suppose all you know is the type of transformations you want to
apply: `rotation`, `scale` or `translation` and the parameters.

So how do you go from `scale by a factor of 2` and `rotate 90 degrees clockwise`
to a transformation matrix?

Well the answer is:

{{< img src="/img/matrices-java-and-android/moar-math-stuff.jpg"
alt="Moar math stuff with smiling cat meme" width="60%">}}

### More math stuff

Well I'm sorry but you'll need a little bit more knowledge about matrices first.

More particularly you'll need to read [this course on *Matrices as
transformations* (which is full of fancy plots and
animations)][khan-matrices-transform] and particularly its last part: ***Representing two dimensional
linear transforms with matrices***.

Come back here once you've read it, or it's gonna hurt...

---

Ok I suppose you've read the course I linked to above, so in this course we've
learned that a position vector `$ \begin{bmatrix} x\\  y \end{bmatrix} $` can be
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
\end{bmatrix} $`.

We've also learned that
`$ \begin{bmatrix} \color{Green} a\\ \color{Green} c \end{bmatrix} $`
and `$ \begin{bmatrix} \color{Red} b\\ \color{Red} d \end{bmatrix} $` are the
position vectors where
`$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 1 \end{bmatrix} $`
and `$ \begin{bmatrix} \color{Red} 1\\ \color{Red} 0 \end{bmatrix} $` will
land respectively after the matrix
`$ \mathbf{A} = \begin{bmatrix} \color{Green} a & \color{Red} b\\
\color{Green} c & \color{Red} d \end{bmatrix} $`
has been applied.

And that given the same transformation,
`$ \begin{bmatrix} x\\ y \end{bmatrix} $` will land on
`$ \begin{bmatrix} \color{Green} a x + \color{Red} b x\\ \color{Green} c x + \color{Red} d y \end{bmatrix} $`.

If you don't understand this conclusion, read again, take your time.

Now remember, our goal is to determine what `$ \mathbf{A} $` is, because we
know the transformation we want to apply bu we're searching for the mattrix we
should apply to our position vector(s) in order to transform our graphics.

Let's take an example, suppose I want to zoom in an image by a factor of 2.

Now I can easily determine what position
`$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 1 \end{bmatrix} $`
and `$ \begin{bmatrix} \color{Red} 1\\ \color{Red} 0 \end{bmatrix} $` will
land on after the transformation (just draw it, it actually helps):

`TODO` Insert picture

* `$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 1 \end{bmatrix} $` will
  land on
  `$ \begin{bmatrix} \color{Green} 0\\ \color{Green} 2 \end{bmatrix} $`
* `$ \begin{bmatrix} \color{Red} 1\\ \color{Red} 0 \end{bmatrix} $` will
  land on
  `$ \begin{bmatrix} \color{Red} 2\\ \color{Red} 0 \end{bmatrix} $`

In summary we have
`$ \begin{bmatrix} x\\ y \end{bmatrix} =  $`

[Matrix.postScale]: https://developer.android.com/reference/android/graphics/Matrix.html#postScale(float,%20float,%20float,%20float)
[climbing-away]: https://play.google.com/store/apps/details?id=fr.climbingaway
[matrix]: https://en.wikipedia.org/wiki/Matrix_(mathematics)
[khan-alg-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices
[khan-intro-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-intro-to-matrices/a/intro-to-matrices
[khan-addsub-matrices]: https://www.khanacademy.org/bigbingo_redirect?continue=https%3A%2F%2Fwww.khanacademy.org%2Fmath%2Falgebra-home%2Falg-matrices%2Falg-adding-and-subtracting-matrices%2Fv%2Fmatrix-addition-and-subtraction-1&conversion_ids=condensed_tutorial_title_click
[khan-mult-matrices]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-multiplying-matrices-by-matrices/a/multiplying-matrices
[khan-defined-vid]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/v/defined-and-undefined-matrix-operations
[khan-defined-course]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-properties-of-matrix-multiplication/a/matrix-multiplication-dimensions
[khan-matrices-transform]: https://www.khanacademy.org/math/algebra-home/alg-matrices/alg-matrices-as-transformations/a/matrices-as-transformations
