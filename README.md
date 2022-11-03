# Random Walk Simulation in Clojure

## What is a random walk simulation?

Generally speaking, in a random walk simulation, particles wander around an environment
in random directions until they collide with other particles and form structures. In real world applications, this type of simulation can be useful for modeling chemical
interactions, such as the formation of crystals and snowflakes.

In this case, the project is simply a fun way to learn and become comfortable with Clojure. 
Especially since the process creates a visually interesting result! 

As a learning project, the simulation is simplified: we start with a fixed particle, and release particles 
until they end up next to the fixed particle. At that point, they also become "fixed" in place,
and other wandering particles can settle next to them as well. This results in neat
organic-looking structures that resemble snowflakes, lightning, or trees!

## How to run?

This project was made with Leiningen, so execute `lein run` to begin!

Tests are also available with `lein test`.