// Constructor
function Point(px, py) {
    // Private
    var x = px;    // integer scalar 
    var y = py;    // integer scalar 

    // Public (privileged for function, public for member with 'this' keyword)
    this.getX = function() {
        return x;
    }

    this.getY = function() {
        return y;
    }
}

function Graph2D(p1, p2) {
    var point1 = p1;    // `Point`
    var point2 = p2;    // `Point`

    this.getPoint1 = function() {
        return point1;
    }

    this.getPoint2 = function() {
        return point2;
    }

    this.getDistance = function() {
        var distanceX = point2.getX() - point1.getX();
        var distanceY = point2.getY() - point1.getY();
        return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
    }
}

function Shape() {
    var vertices = undefined;    // Array of `Point`s
    var edges    = undefined;    // Array of `Graph2D`s

    this.isFiniteVertices = undefined;
    this.getArea = undefined;
    this.setVertices = function(pointArray) {
        //#ifdef _DEBUG_
        if (!Array.isArray(pointArray)) {
            console.error("pointArray must be an Array, but: " + pointArray + " is given");
            return;
        }
        for(var i = 0, limit = pointArray.length; i < limit; i++) {
            var point = pointArray[i];
            if (!(point instanceof Point)) {
                throw new TypeError("!instanceof Point: " + point + " at index " + i);
            }
        }
        //#endif
        vertices = pointArray;
    }

    this.getVertices = function() {
        return vertices;
    }

    this.setEdges = function(vectorArray) {
        edges = vectorArray;
    }

    this.getEdges = function() {
        return edges;
    }
}

function Polygon(verticesArray) {
    this.setVertices(verticesArray);
    var vectorArray = []
    for(var i = 0, limit = verticesArray.length; i < limit; i++) {
        var p1;
        var p2;
        if (i === limit - 1) {
            p1 = verticesArray[i];
            p2 = verticesArray[0];
        } else {
            p1 = verticesArray[i];
            p2 = verticesArray[i + 1];
        }

        vectorArray.push(new Graph2D(p1, p2));
    }
    this.setEdges(vectorArray);
}
// Simple type inheritance
Polygon.prototype = new Shape();
// This for field inheritance
Polygon.prototype.isFiniteVertices = function() {
    return true;
}

function RegularPolygon(verticesArray) {
    Polygon.call(this, verticesArray);
    var edges = this.getEdges();
    var distance = 0;
    var isIrregular = false;
    var graph2d;
    for(var i = 0, limit = edges.length; i < limit; i++) {
        graph2d = edges[i];
        // approximation under 8th fractions
        var newDistance = Math.round10(graph2d.getDistance(), -8);
        if (distance == 0) {
            distance = newDistance;
        } else if (distance != newDistance) {
            isIrregular = true;
            break;
        }
    }

    if (isIrregular) {
        console.error("Distance differs to previous one: " + graph2d.getDistance() + " vs " + distance);
        console.error(graph2d);
    }
}
// ES5 Way of type inheritance
RegularPolygon.prototype = Object.create(Polygon.prototype);
RegularPolygon.prototype.constructor = Polygon;
