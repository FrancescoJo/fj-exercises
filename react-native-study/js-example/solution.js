function Triangle(verticeArray) {
    RegularPolygon.call(this, verticeArray);
}
Triangle.prototype = Object.create(RegularPolygon.prototype);
Triangle.prototype.constructor = RegularPolygon;
Triangle.prototype.getArea = function() {
    // 'any' side length must be same to all others
    var side = this.getEdges()[0].getDistance();
    // area: sqrt(3) / 4 * side^2
    return (Math.sqrt(3) / 4) * Math.pow(side, 2);
}

function Square(verticeArray) {
    RegularPolygon.call(this, verticeArray);
}
Square.prototype = Object.create(RegularPolygon.prototype);
Square.prototype.constructor = RegularPolygon;
Square.prototype.getArea = function() {
    // 'any' side length must be same to all others
    var side = this.getEdges()[0].getDistance();
    // area: side^2
    return Math.pow(side, 2);
}

function RegularPolygonFactory() { }
// Static member of 'RegularePolygonFactory'
RegularPolygonFactory.create = function(verticeArray) {
    // TODO: implement this
    var nVertices = verticeArray.length;
    if (nVertices === 3) {
        return new Triangle(verticeArray);
    } else if (nVertices === 4) {
        return new Square(verticeArray);
    } else {
        console.error("Only 3 or 4 vertices are allowed");
        return null;
    }
}

testRegularPolygon();