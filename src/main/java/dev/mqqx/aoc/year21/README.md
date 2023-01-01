# Advent of Code 2021 

Solutions for Advent of Code 2021

## Day 1

Loops with counters.

## Day 2

Loops, with string replacement as well some counters.

## Day 3

For part 1 using mainly plain looping with some counters.

For part 1 using `^` operator to calculate whether zeros or ones need to be removed depending on most common bit and most relevant one.

## Day 4

Using a `BingoNumber` record to safe the state on a bingo board after each drawn number. Therefor just used lots of plain iterations over all boards to find the first/last winning board.

## Day 5

Solved by extending own `Point` class to provide util methods for horizontal (`y1==y2`), vertical (`x1==x2`) or diagonal (`abs(x2 - x1)==abs(y2 - y1)`) points between `this` and a given point.

## Day 6

At first, I just used an `AtomicLong` for each fish and counted them separately, which resulted in an OOM-Error on part 2.

Therefor I migrated to an `AtomicLongMap` (Guava) with the day as the key and the count of fish on that day as value. This works by using `getAndAdd` to either just increase the existing counter or put a new one in the temporary map.

## Day 7

Start at median and increase/decrease possible height until minimum fuel does not get any lower.

Use same logic for part 2 with added `sumToN` call, to count for the increased fuel cost the further the crab needs to move.

## Day 8

For part 1 I removed the left side of the text of the `|` sign and used the regex `\b(?:[a-z]{2,4}|[a-z]{7})\b` to count all strings where the output had a length of 2, 3, 4 or 7 which represents the numbers 1, 4, 7 and 8.

As for part 2 one needs to recognize all numbers, so they can be summed up. Therefor I came up with some unique constrains regarding the length and already found numbers. As each number-string will exactly once on the left side, already mapped numbers can be removed from the set of numbers to parse.

## Day 9

Parse the height map into a 2d-array and check all numbers if they are surrounded by higher ones. If that's the case then add it (+ an additional 1) to the sum.

For the 2nd part check connected top and left basins and optionally merge them together.
Count the saved basins in a `AtomicLongMap` and multiply the three largest basis counts.

## Day 10
## Day 11
## Day 12
## Day 13
## Day 14
## Day 15
## Day 16
## Day 17
## Day 18
## Day 19
## Day 20
## Day 21
## Day 22
## Day 23
## Day 24
## Day 25