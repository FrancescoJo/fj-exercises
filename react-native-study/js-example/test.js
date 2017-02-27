function assertTrue(desc, expr) {
    if (expr) {
        console.debug("[OK   ] " + desc)
    } else {
        console.error("[ERROR] " + desc);
    }
}

function testRegularPolygon() {
    var triangle = RegularPolygonFactory.create([
        new Point(0, 0), new Point(2, 0), new Point(1, Math.sqrt(3))
    ]);
    assertTrue("Triangle must be a subtype of RegularPolygon",
            triangle instanceof RegularPolygon);
    assertTrue("Triangle must be a subtype of Polygon",
            triangle instanceof Polygon);
    assertTrue("Triangle must be a subtype of Shape",
            triangle instanceof Shape);
    assertTrue("Triangle's nVertices must be 3",
            triangle.getVertices().length === 3);
    // Approx. 1.73205
    assertTrue("Triangle's area must be sqrt(3) / 4 * side^2",
            Math.round10(triangle.getArea(), -5) == 1.73205);

    var square = RegularPolygonFactory.create([
        new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)
    ]);
    assertTrue("Square must be a subtype of RegularPolygon",
            square instanceof RegularPolygon);
    assertTrue("Square must be a subtype of Polygon",
            square instanceof Polygon);
    assertTrue("Square must be a subtype of Shape",
            square instanceof Shape);
    assertTrue("Square's nVertices must be 4",
            square.getVertices().length === 4);
    assertTrue("Square's area must be side ^ 2",
            Math.round10(square.getArea(), -5) == 1);
}
