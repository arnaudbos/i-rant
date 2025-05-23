---
title: Water Pouring Problem in Clojure
date: 2017-11-28T06:23:11+01:00
description: Solving the water pouring problem in Clojure and detailing my approach at the REPL
parent: blog
categories: ["clojure"]
tags: ["clojure", "REPL", "kotlin"]
draft: false
seoimage: img/water-pouring-in-clojure/die-hard-3-water-puzzle.jpg
highlight: true
klipse: true
gallery: true
---

{{< img src="/img/water-pouring-in-clojure/die-hard-3-water-puzzle.jpg" title="Die Hard with a Vengeance — Water puzzle scene" alt="Caption of the water puzzle scene in Die Hard with a Vengeance" width="100%" >}}

-----

This post is the first or a series where I explore Clojure features
and start a discussion with my colleague [Igor][igor] about [Clojure][clojure]
and [Kotlin][kotlin].

This summer, [Igor][igor] offered to make a live Kotlin demo of a nice problem
he's been [working on][kotlin-pouring] that can serve as a fine exercise to
discover and experiment a programming language: the
[Water pouring puzzle][water-puzzle].

In this article I talk about the ***programming style*** I've
used to find my solution: a data oriented, bottom-up approach, at the
REPL.

-----

Igor's [guideline][kotlin-pouring-guideline] offers a good starting point
on how OO and/or static typing (including myself when I switch to Java for
instance) would solve this problem: model the world.  
Notice how, by following this recipe, you don't get to handle or transform
any data until point 15 of about 30.

Although I understand how this is more usual, familiar and maybe the most
straightforward way to do things for a majority of developers, to me this is a
major drawback. It means that half of the time you are spending laying out
things, hoping the Lego blocks will fall into place, thinking about types and
safety when all you should be thinking about should be (IMO) how do I solve
this particular problem.

After a few years of [REPL][good-repl] [driven development][repl-driven-dev] I
know that I have a good tool to functionally, expressively and
incrementally solve a problem.

This post is basically a transcript of my thought process as I was writing
a solver to the Water pouring puzzle at the REPL.  
It was interesting to take the time to reflect and watch myself work as
I was proceeding, it took more time but it was definitely an interesting
experiment.

----

I setup the boilerplate first. (leiningen, dependencies, etc) and a
namespace to work in:

{{< klipse >}}
(ns clojure-pouring.repl)
{{< /klipse >}}

At the REPL, the fundamental principle is: ***exploratory programming***.  
I don't know how to find the solution to this problem (yet!),
all I have at this point is a namespace.

I must begin somewhere, so let's start with some data: a glass has a total
capacity and a current state (`0` by default), I need a collection of those.

I start at the REPL by declaring `inital-state`:

{{< klipse >}}
(def initial-state
  [{:capacity 5
    :current 0}
   {:capacity 3
    :current 0}
   ])

initial-state
{{< /klipse >}}

Then `final-state` to illustrate the target:

{{< klipse >}}
(def final-state
  [{:capacity 5
    :current 4}
   {:capacity 3
    :current 0}
   ])

final-state
{{< /klipse >}}

Pretty easy: each glass is a `map` and I put two of those in a `vector`.  
Just thinking about the amount of code I would have to write in Java or Kotlin
just to get a short sample of data like this gives me goosebumps.

Kotlin [might](https://discuss.kotlinlang.org/t/plans-for-collection-literals/2049),
at [some point](https://blog.jetbrains.com/kotlin/2017/06/kotlin-future-features-survey-results/),
have collection literals similar to those, or not.  
There seems to be a debate inside the community whether this is a desired
feature or not, despite the clear winner of the community survey.

Communicating intent is an important part of a language and Kotlin's debate
around collection literals seems to fall in the category of convenient syntax
vs intent. Why?  
Because Kotlin's designers have made the choice to support both mutable and
immutable collections as first class citizens. So how do you define what `{}`
is or `[]` is? Is it mutable or immutable?  
The debate also seem to include considerations such as ordered/unordered and the
choice of syntax between lists and sets. Good luck figuring that out.

Clojure is based on first-class persistent data-structures so mutation is not
and option, and its collection literals are easily identified:

* `()` for lists (PersistentList)
* `[]` for vectors (PersistentVector)
* `{}` for a maps (PersistentHashMap)
* `#{}` for sets (PersistentHashSet)

Want ordered/unordered variants? array-maps? queues? Sure, just use the
appropriate constructor, the collections you will use the most are available
as literals and it's a burden not put on you, the developer.  
View it as *"only convenient"* if you want to ditch the argument and discussion
altogether, but with mutation out of the way a whole class of problems are gone
once again, and the benefit of being able to just copy and paste
around printed representations of data-structures (in REPL or in logs) and
use them verbatim as code is priceless.

Alright, so the actions allowed to be performed on a glass are either to *pour*
some quantity out of it, or to *fill* it with some quantity.

So I implement a `pour` function first:

{{< klipse >}}
(defn pour
  ([glass]
   (assoc glass :current 0))
  ([glass quantity]
   (let [{:keys [current]} glass]
     (assoc glass :current (- current
                              (min current quantity))))))
{{< /klipse >}}

This is a multi-arity function:

* The first implementation takes a glass as its only parameter and returns a
  <a name="anew"></a>new<sup>[1](#new)</sup> empty glass of the same capacity.
* The second implementation takes a glass as its first parameter and a quantity
  as its second parameter and returns a new glass *poured out of* the given
  quantity or *poured out empty* if it contains less than the given quantity.
* In the second arity, I *destructure* the first parameter (the glass) which
  is a map, in order to get its `current` key only, as I don't care about its
  `capacity`.
* `assoc` is ([for short](https://clojuredocs.org/clojure.core/assoc))
  a standard library function that returns a new map containing the new
  `key/value` mapping.

Don't worry about the prefix notation, it's not difficult, it's just different,
you're not
*[familiar](https://www.infoq.com/presentations/Simple-Made-Easy)* with it, and
you'll get used to it.

Test `pour`:

{{< klipse >}}
(println (pour {:capacity 5 :current 4}))
(println (pour {:capacity 8 :current 4} 2))
(println (pour {:capacity 8 :current 4} 5))
{{< /klipse >}}

* Pouring out empty a glass of capacity `5` returns an empty glass of capacity
  `5`.
* Pouring out `2` from a glass of capacity `8` containing `4` returns
  a glass of capacity `8` containing `2`.
* And pouring out `5` from a glass of capacity `8` containing `4` just returns
  and empty glass, not `-1`.

Ignore the last `nil` in the results, it's just the live code evaluation
plugin that prints it out as the result of the last `println` call.  
Go ahead and play with it a little, even change the implementation above
if you will.

Now the `fill` function:

{{< klipse >}}
(defn fill
  ([glass]
   (assoc glass :current (:capacity glass)))
  ([glass quantity]
   (let [{:keys [capacity current]} glass]
     (assoc glass :current (+ current
                              (min (- capacity current) quantity))))))
{{< /klipse >}}

This is also a multi-arity function:

* The first implementation fills a glass up to its maximum capacity.
* The second implementation fills a glass *up to* its maximum capacity given
  a quantity to fill it with.
* Both return a new glass of course (not a copy!!!), we're in ***immutable***
  land here.

Test `fill`:

{{< klipse >}}
(println (fill {:capacity 5 :current 0}))
(println (fill {:capacity 8 :current 4} 1))
(println (fill {:capacity 8 :current 7} 2))
{{< /klipse >}}

* Filling an empty glass returns a glass full.
* Filling `1` into a glass of capacity `8` that already contains `4` returns a
  glass of capacity `8` containing `5`.
* And filling `2` into a glass of capacity `8` that already contains `7` returns
  a glass of capacity `8` that is full, not `9`.

{{< gallery >}}
  {{% galleryimage file="/img/water-pouring-in-clojure/jug2.jpg"
  size="728x485" width="390px" %}}
  {{% galleryimage file="/img/water-pouring-in-clojure/jug3.jpg"
  size="728x485" width="390px" %}}
{{< /gallery >}}

Let's take a break here.

-----

One of the major benefits of a good REPL (and Clojure's REPL is a good one),
is the ability to explore: write one function at a time, execute it
**right away** with a small amount of data, creating a really short
**feedback loop** in order to **explore** multiple solutions.

In this blog post I use [Klipse][klipse] in order to present my iterative
process and **let you play with the code**.  
This is nice for you to get a feel and try different inputs to my functions,
or write different implementations if you want to.

A good REPL is superior to TDD because it lets you write (or better: keep)
tests that matter, get the result out of a function call and use it again as
data that can populate your *real* tests.

But if you've tried other REPLs (ruby, python, node, etc.), you may be
thinking that a REPL is not practical, because it forces you to copy/paste
code all the time, go back and forth in you REPL's history in order to
retrieve past results and implementations just to change a few characters.

And this is where tooling is important.

Lots of Clojure developers use emacs (or cursive, an Intellij plugin) and
benefit from its integration with the REPL.  
I, personnally, have been enjoying [Light Table][lighttable] since 2013, and
this is a screenshot of my laptop while I'm working at the REPL:

{{< gallery title="Light Table instaREPL" >}}
  {{% galleryimage file="/img/water-pouring-in-clojure/screenshot.png"
  size="1440x900" title="Light Table instaREPL"
  width="100%" %}}
{{< /gallery >}}

Do you see the "blue" data? This is the result of evaluating a Clojure
expression in the editor itself.  
This alone has made it difficult for me to switch to another editor ever since.

At the REPL, I type in **data**, I pass it to **functions** and I get the
**result** (which I use later as new input/expected output for other tests)
in front of my eyes.  
I define **new functions** and make extensive use of *"variable shadowing"* to
**modify the implementation of existing functions**.

No classes, no types, no FactoryFactoryAbstractDelegateProxyPattern:
just data and functions...  
I can hear you, static typing addict: **BUT IT IS NOT SAFE!**  
Yes, I know, I just don't care at this point! I told you, I'm **exploring**.
I want to go fast, iterate quickly, not model the whole universe and bang my
head against the type checker.

I can just rely on my tests for now and use simple assertions or
[specifications](https://clojure.org/about/spec) later (or now, but right now
I'm not, obviously) to refine.

I've never had a shortest feedback loop in any other language I've used, this is
pure gold.

-----

Let's continue our problem solving.

I have a pouring and a filling function.  
Now I'd need a function that takes a collection of glasses (state),
apply an action (commonly called a 'move' in the current domain language)
and return a new, updated collection of glasses (new state).

What characterizes a move? It's either an `empty`, `pour` or `fill` action
associated with glass indices:

* empty `from` the nth glass
* fill `to` the nth glass
* pour `from` the mth glass `to` the nth glass

Expressivity matters, so maybe a move would be best represented by a map.  
A map with a type that characterizes the move (`empty`, `fill`, `pour`) and
subjects that indicate the source / target / both of the move (`from`, `to`):

{{< klipse >}}
(println "Empty glass 0:" {:type :empty :from 0})
(println "Fill glass 1:" {:type :fill :to 1})
(println "Pour glass 1 into glass 0:" {:type :pour :from 1 :to 0})
{{< /klipse >}}

I'm beginning to think that a bit of *Ad-hoc polymorphism* would
be great in order to dispatch on the type of move: let's use
*multimethods*.

{{< klipse >}}
(defmulti ->move
  "Apply a move to the given state and return the new state."
  (fn [state move] (:type move)))
{{< /klipse >}}

The `defmulti` macro defines a new multimethod with the associated
`dispatch function`.  
Which means that the `->move` function will take two parameters, and the
dispatch function will *choose* the appropriate implementation.

In this case I dispatch based on the value associated to the `:type` key in the
`move` parameter.  
A `:empty` move will dispatch to the `->move :empty` multimethod.  
A `:fill` move will dispatch to the `->move :fill` multimethod.  
A `:pour` move will dispatch to the `->move :pour` multimethod.

{{< klipse >}}
; Empty the glass at index from
(defmethod ->move :empty
  [state {:keys [from]}]
  (update-in state [from] pour))

; Fill the glass at index to
(defmethod ->move :fill
  [state {:keys [to]}]
  (update-in state [to] fill))

; Pour the glass at index from into the glass at index to
(defmethod ->move :pour
  [state {:keys [from to]}]
  (let [quantity (min (get-in state [from :current])
                      (- (get-in state [to :capacity])
                         (get-in state [to :current])))]
    (-> state
        (update-in [from] pour quantity)
        (update-in [to] fill quantity))))
{{< /klipse >}}

`update-in` is a [standard library](https://clojuredocs.org/clojure.core/update-in)
function that takes a data structure as a first parameter and "updates"
(immutable, remember?) the value nested at the path given by the second
parameter by applying to it the function supplied as the third parameter...  
Don't pretend you didn't understand, just reread carefully.

Let's take our `->move :empty` multimethod as an example.  
Since our move is an `:empty`, we destructure this parameter to get the
value at the `:from` key: the index of the glass in `state` that we want to
empty.  
Once the glass has been fetched, `update-in` will evaluate the `pour` function
with the glass as its sole parameter.  
Since I've implemented the 1-arity of the `pour` function so that it just
returns a new empty glass of same capacity, this result will *replace*
the glass in the `state`.

Let's test `->move` to "manually" go from `initial-state` to `final-state`:

{{< klipse >}}
(-> initial-state
    (->move {:type :fill :to 1})
    (->move {:type :pour :from 1 :to 0})
    (->move {:type :fill :to 1})
    (->move {:type :pour :from 1 :to 0})
    (->move {:type :empty :from 0})
    (->move {:type :pour :from 1 :to 0})
    (->move {:type :fill :to 1})
    (->move {:type :pour :from 1 :to 0})
    )
{{< /klipse >}}

It works!

I must now implement a function that, when given a collection of glasses,
returns a collection of possible moves that can be applied to this state in
order to produce a *meaningful* new collection of glasses.

Meaningful as in:

* not emptying an empty glass,
* not filling a glass that is full, and
* not pouring into 'itself', which gets us nowhere

{{< klipse >}}
(defn glasses->index
  "Filter the glasses by the filter-fn function and
  return the index the filtered item had in the
  glasses collection."
  [glasses filter-fn]
  (->> glasses
       (map-indexed #(vector %1 %2))
       (filter (fn [[idx value]] (filter-fn value)))
       (map first)))
{{< /klipse >}}

I'm not going to explain everything, just look at the input and output in this
test of `glasses->index`:

{{< klipse >}}
(println "Indices of non-empty glasses:"
         (glasses->index final-state (comp pos? :current)))
(println "Indices of glasses of capacity 3:"
         (glasses->index final-state (comp #(= 3 %) :capacity)))
{{< /klipse >}}

The *"indexing"* works, now the function for listing moves:

{{< klipse >}}
(defn available-moves
  "Return the list of valid moves from current state of glasses."
  [glasses]
  (let [non-empty (glasses->index glasses (comp pos? :current))
        non-full (glasses->index glasses #(< (:current %) (:capacity %)))]
    (concat
      (map #(hash-map :type :empty :from %) non-empty)
      (map #(hash-map :type :fill  :to   %) non-full)
      ; I really like this one
      (for [from non-empty to non-full :when (not= from to)]
        {:type :pour :from from :to to}))))
{{< /klipse >}}

Concatenate the list of moves able to empty the non-empty glasses, fill the
non-full glasses and pour the non-empty glasses into the distinct non-full
glasses.

Test `available-moves`:

{{< klipse >}}
(println (available-moves initial-state))
(println (available-moves final-state))
{{< /klipse >}}

I can get the list of moves that are practicable, now let's
explore the next glasses available from the current glasses.

{{< klipse >}}
(defn explore
  [glasses moves]
  (map (partial ->move glasses) moves))
{{< /klipse >}}

Test `explore`:

{{< klipse >}}
(explore initial-state (available-moves initial-state))
{{< /klipse >}}

Given a list of glasses (a state), returns the list of glasses I could get to
(next states) by applying the moves that are available from my current state.

{{% fold id="explore" title="Exploration" %}}

I won't need this function because in order to find the list of moves
leading from a state to another I must keep the previous moves in memory when
exploring further...

`explore` is not enough, but at least I know how to explore the spectrum
of all the possible next states for a given state and list of moves.

See? I've *explored*, I've made an experiment with a function mapped over
some data. And it didn't cost much because my feedback loop is really short.

I've realized this path is not going to help, so I can just discard it and
refine my understanding of the steps I need to take to reach my goal.

{{% /fold %}}

Given a `node` containing glasses and the sequence of moves leading to
them, return a list of successor nodes.

{{< klipse >}}
(defn expand
  [{:keys [glasses moves] :as node}]
  (let [next-moves (available-moves glasses)]
    (map #(hash-map :glasses (->move glasses %)
                    :moves (conj moves %))
         next-moves)))
{{< /klipse >}}

I evaluate the moves available from the current node's glasses and for each
one of them I return a new node consisting of the next state of glasses and
the augmented list of move leading to it.

Test `expand`:

{{< klipse >}}
(println
  (expand
    {:glasses [{:capacity 5 :current 5} {:capacity 3 :current 0}]
     :moves [{:type :fill :to 0}]}))
{{< /klipse >}}

Nice, given glasses and the `history` of moves that led from an initial
state to these glasses, I can get the list of next states that are
*possible*, with their respective history of moves.

But at some point I might reach a state that I have already visited in the
past... So it would be interesting to keep an index of the states that I
have already visited in order to query it and avoid useless computation.

Let's *"backtract"* the already visited nodes:

{{< klipse >}}
(defn backtrack
  "Returns true if a node has been visited."
  [visited {:keys [glasses]}]
  (not (contains? visited glasses)))
{{< /klipse >}}

Test `backtrack`:

{{< klipse >}}
(backtrack #{[{:capacity 5 :current 0} {:capacity 3 :current 0}]}
           {:glasses [{:capacity 5 :current 5} {:capacity 3 :current 0}]
            :moves [{:type :fill :to 0}]})
{{< /klipse >}}

The first parameter of `backtrack` is a set, so `contains?` is `O(1)`, not
`O(n)`.

I'm almost there, right?  
But I must stop searching at some point, so let's identify a solution among
a set of candidates:

{{< klipse >}}
(defn has-solution?
  [target successors]
  (some #(when (= target (:glasses %)) (:moves %))
        successors))
{{< /klipse >}}

`some` is a [standard library](https://clojuredocs.org/clojure.core/some)
function that returns the first logical true value of the predicate function
result.  
My predicate function returns the list of moves leading to the current node
when the glasses of the node match the target glasses state, so if the
`successors` collection contains the solution, the list of moves to get
there is returned.

Test `has-solution?`:

{{< klipse >}}
(println "Does not have solution:"
  (has-solution?
    [{:capacity 5 :current 4} {:capacity 3 :current 0}]
    []))

(println "The solution (list of moves) is:"
  (has-solution?
    [{:capacity 5 :current 4} {:capacity 3 :current 0}]
    [{:glasses [{:capacity 5 :current 0}] :moves []}
     {:glasses [{:capacity 5 :current 4} {:capacity 3 :current 0}] :moves [:foo :bar]}]))
{{< /klipse >}}

And now the final part: I must compose the functions I have written so far
in order to implement a solver.

This is *bottom-up*. I know I have all the parts of my solver in place and
I have tested most of them along the way.

Let's loop over glasses nodes, keeping track of the already visited ones,
searching for a solution leading to the target state:

{{< klipse >}}
(require '[clojure.set :as set])

(defn solver
  "Given a target state, returns a function that will solve the water pouring
  problem for an initial state"
  [target]
  (let [has-solution-fn? (partial has-solution? target)]
    (fn [glasses]
      ; Start with an empty set of visited states and an empty list of moves
      (loop [visited #{} nodes [{:glasses glasses :moves []}]]
        (if-let [solution (has-solution-fn? nodes)]
          solution
          (let [successors (mapcat #(expand %) nodes)
                valid-successors (filter (partial backtrack visited) successors)
                newly-visited (into #{} (map :glasses valid-successors))]
            (recur (clojure.set/union visited newly-visited)
                   valid-successors)))))))
{{< /klipse >}}

Test `solver`:

{{< klipse >}}
(def simple-solver
  (solver [{:capacity 5 :current 4}
           {:capacity 3 :current 0}]))

(simple-solver [{:capacity 5 :current 0}
                {:capacity 3 :current 0}])
{{< /klipse >}}

Test it again with a (seemingly) more complicated use case:

{{< klipse >}}
(def less-simple-solver
  (solver [{:capacity 8 :current 4}
           {:capacity 5 :current 0}
           {:capacity 3 :current 0}]))

(less-simple-solver [{:capacity 8 :current 0}
                     {:capacity 5 :current 0}
                     {:capacity 3 :current 0}])
{{< /klipse >}}

-----

Functionally I could stop here because the problem is solved. But there are
some points that bother me, some `vars` names that don't reflect what
piece of information they carry or some functions that could be broken into
smaller ones.

Let's refactor a little:

Refactor `->move` to better reflect domain:

{{< klipse >}}
(defmulti ->move
  "Apply a move to the given state and return the new state."
  (fn [glasses move] (:type move)))

(defmethod ->move :empty
  [glasses {:keys [from]}]
  (update-in glasses [from] pour))

(defmethod ->move :fill
  [glasses {:keys [to]}]
  (update-in glasses [to] fill))

(defmethod ->move :pour
  [glasses {:keys [from to]}]
  (let [quantity (min (get-in glasses [from :current])
                      (- (get-in glasses [to :capacity])
                         (get-in glasses [to :current])))]
    (-> glasses
        (update-in [from] pour quantity)
        (update-in [to] fill quantity))))
{{< /klipse >}}

Refactor `expand` by extracting the node builder:

{{< klipse >}}
(defn make-node
  [glasses moves]
  {:glasses glasses
   :moves moves})

(defn expand-node
  [{:keys [glasses moves]}]
  (let [next-moves (available-moves glasses)]
    (->> next-moves
         (map #(make-node (->move glasses %) (conj moves %))))))
{{< /klipse >}}

Refactor `solver` by decomposing functions:

Let's create a `make-glass` utility function:

{{< klipse >}}
(defn make-glass
  ([capacity]
   (make-glass capacity 0))
  ([capacity current]
   {:capacity capacity :current current}))
{{< /klipse >}}

Test `make-glass`:

{{< klipse >}}
(make-glass 5)
{{< /klipse >}}

{{< klipse >}}
(make-glass 8 3)
{{< /klipse >}}

Now a `initialize` function to create a vector of glasses from capacities or
capacities + quantities:

{{< klipse >}}
(defn initialize
  ([capacities]
   (vec (map make-glass capacities)))
  ([capacities quantities]
   (vec (map make-glass capacities quantities))))
{{< /klipse >}}

Test `initialize`:

{{< klipse >}}
(initialize [8 5 3])
{{< /klipse >}}

{{< klipse >}}
(initialize [8 5 3] [4 0 0])
{{< /klipse >}}

Let's extract the part of the solver that find the successors of a collection
of nodes into `find-successors`:

{{< klipse >}}
(defn find-successors
  [nodes]
  (mapcat #(expand-node %) nodes))
{{< /klipse >}}

Test `find-successors`:

{{< klipse >}}
(find-successors [{:glasses [{:capacity 5 :current 0} {:capacity 3 :current 0}] :moves []}])
{{< /klipse >}}

Let's extract the part of the solver that filters out the successors that have
already been visited into `filter-successors`:

{{< klipse >}}
(defn filter-successors
  [successors visited]
  (filter (partial backtrack visited) successors))
{{< /klipse >}}

Test `filter-successors`:

{{< klipse >}}
(filter-successors
  [{:moves [{:type :fill :to 0}] :glasses [{:capacity 5, :current 5} {:capacity 3, :current 0}]}
   {:moves [{:type :fill, :to 1}], :glasses [{:capacity 5, :current 0} {:capacity 3, :current 3}]}]
  #{[{:capacity 5, :current 5} {:capacity 3, :current 0}]})
{{< /klipse >}}

We're almost done. Let's create a function to only keep a distinct set of
successors from a collection of candidates with `get-unique-glasses`.

{{< klipse >}}
(defn distinct-glasses
  [successors]
  (into #{} (map :glasses successors)))
{{< /klipse >}}

Test `distinct-glasses`:

{{< klipse >}}
(distinct-glasses
  [{:moves [{:type :fill :to 0}] :glasses [{:capacity 5, :current 5} {:capacity 3, :current 0}]}
   {:moves [{:type :fill, :to 1}], :glasses [{:capacity 5, :current 5} {:capacity 3, :current 0}]}])
{{< /klipse >}}

And now we can rewrite `solver` with these new pieces.

{{< klipse >}}
(defn solver
  [capacities quantities]
  (let [initial (initialize capacities)
        target (initialize capacities quantities)
        has-solution-fn? (partial has-solution? target)]
    (fn []
      (let [first-node (make-node initial [])]
        (loop [visited #{initial} nodes [first-node]]
          (if-let [solution (has-solution-fn? nodes)]
            solution
            (let [successors (find-successors nodes)
                  valid-successors (filter-successors successors visited)
                  unique-glasses (distinct-glasses valid-successors)]
              (recur (clojure.set/union visited unique-glasses)
                     valid-successors))))))))
{{< /klipse >}}

Test `solver`:

{{< klipse >}}
(def simple-solver (solver [5 3] [4 0]))
(println (simple-solver))
{{< /klipse >}}

{{< klipse >}}
(def less-simple-solver (solver [8 5 3] [4 0 0]))
(println (less-simple-solver))
{{< /klipse >}}

Implement `-main`:

{{< klipse >}}
(defn -main
  [& args]
  (assert (even? (count args)))
  (let [input (map #(Integer. (re-find  #"\d+" %)) args)
        solver-fn (apply solver (split-at (/ (count input) 2) input))]
    (println (solver-fn))))
{{< /klipse >}}

----

It's been a pretty long article in the end. Together we've implemented a
function to solve the water pouring problem for an arbitrary list of glasses
of some quantity in Clojure.

You can find the file I've used to write this exploratory work
[here][clj-water-pouring-repl], and the final version of the solver without
all the evaluations [here][clj-water-pouring-core].

The thing that is the most interesting to me here is not the number of lines,
the safety or the provability of correctness.  
What I want to stress out is the development at the REPL.

You can see in the repository that I didn't even bother to write tests, because
I don't plan to share this work, publish it or maintain it. Nonetheless it has
been tested.  
I has been tested all the way from the beginning, at each function definition,
with data. You see, I didn't need to use TDD, and I didn't have to write some
temporary `main` function to pass inputs to the command line, and I didn't need
to wait until I've assembled all the code together to try it fingers crossed.

Through the use of immutability, pure functions and first-class data literals
I feel like I can achieve more and iterate more quickly than with any other
language I've used.  
I also argue that the Lisp syntax, although different, is an asset, because
there's no cognitive load associated with it: no magical keywords, no mix of
parents, braces or brackets for structure: just lists as functions and data.

Footnote:

<a name="new">1</a>: This it is not exactly a new glass as in 'a copy' but a
new pointer to a persistent data structure, a concept also know as
*structural sharing* ([go back](#anew))

[kotlin]: https://kotlinlang.org/
[igor]: https://twitter.com/ilaborie
[kotlin-pouring]: https://github.com/ilaborie/kotlin-pouring
[water-puzzle]: https://en.wikipedia.org/wiki/Water_pouring_puzzle
[clojure]: https://clojure.org/
[kotlin-pouring-guideline]: https://github.com/ilaborie/kotlin-pouring#guideline-try-to-implements-with-immutable-data-structure
[good-repl]: http://vvvvalvalval.github.io/posts/what-makes-a-good-repl.html
[repl-driven-dev]: https://vimeo.com/223309989
[klipse]: https://github.com/viebel/klipse
[lighttable]: http://lighttable.com/
[clj-water-pouring-repl]: https://github.com/arnaudbos/clj-water-pouring/blob/master/src/clj_water_pouring/repl.clj
[clj-water-pouring-core]: https://github.com/arnaudbos/clj-water-pouring/blob/master/src/clj_water_pouring/core.clj
