Let a `Shape` object has following traits: 

  1. a set of _vertices_,
  2. a set of _vector of vertices_,
  3. _vector of vertices_ must produce a **closed, Eulerian trail**.
  4. from the rule #3: if we let the numbers of _vertices_ as `n1` and
     numbers of _vector of vertices_ as `n2`, `n1` and `n2` must conform:      
     
     `(n1 == n2) && (n1 >= 3 && n2 >= 3)`

According to the definition, we can derive `Polygon` from it. Polygons are:

  1. have finite set of _vertices_ 
  2. have finite set of _vector of vertices_

Moreover, if we make a `Polygon` that have all magnitudes of _vector of vertices_
are equal, we can call it `Regular Polygon`.

With rules above, write code in `solution.js` to make testRegularPolygon()
invocation does not report any test failures. When you are done, open
`solution.html` in your web browser and enable developer console to verify
results.
