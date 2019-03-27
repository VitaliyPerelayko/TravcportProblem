package by.vit.transportproblemsolve;


import by.vit.model.*;
import com.programmerare.shortestpaths.adapter.yanqi.PathFinderFactoryYanQi;
import com.programmerare.shortestpaths.core.api.*;
import com.programmerare.shortestpaths.core.validation.GraphEdgesValidationDesired;

import java.util.*;

import static com.programmerare.shortestpaths.core.impl.EdgeImpl.createEdge;
import static com.programmerare.shortestpaths.core.impl.GraphImpl.createGraph;
import static com.programmerare.shortestpaths.core.impl.VertexImpl.createVertex;
import static com.programmerare.shortestpaths.core.impl.WeightImpl.createWeight;

public class DistanceMatrix {

    private static final PathFinderFactory pathFinderFactory = new PathFinderFactoryYanQi();

    private Double[][] distanseMatrix;
    private List<Path> pathList;

    public DistanceMatrix(Road[] roads, Point[] points) {
        this.distanseMatrix = new Double[points.length+1][points.length+1];
        this.pathList = new ArrayList();
        createDistanceMatrix(createGraphFromRoads(roads),createArrayOfPointId(points));
    }

    public Double[][] getDistanseMatrix() {
        return distanseMatrix;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    private static Graph createGraphFromRoads(Road[] roads) {
        List<Edge> edges = new ArrayList<>();
        for (Road road : roads) {
            Vertex vertex1 = createVertex(road.getId().getPoint1Id().toString());
            Vertex vertex2 = createVertex(road.getId().getPoint2Id().toString());
            edges.add(createEdge(vertex1, vertex2, createWeight(road.getDistance())));
            edges.add(createEdge(vertex2, vertex1, createWeight(road.getDistance())));
        }
        return createGraph(edges, GraphEdgesValidationDesired.YES);
    }

    private static Long[] createArrayOfPointId(Point[] points) {
        Long[] pointsId = new Long[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsId[i]=points[i].getId();
        }
        return pointsId;
    }

    private static Vertex getVertexById(Graph graph,Long id){

        for(Vertex vertex:graph.getVertices()){
            if (vertex.getVertexId().equals(id.toString())){
                return vertex;
            }
        }
        return null;
        //throw Exception add write to the Log
    }

    private void createDistanceMatrix(Graph graph, Long[] pointsId) {
        PathFinder pathFinder = pathFinderFactory.createPathFinder(graph);
        int pointIdLength = pointsId.length;
        for (int i = 0; i < pointIdLength; i++) {
            this.distanseMatrix[i][i] = 0D;
            this.distanseMatrix[0][i+1] = pointsId[i].doubleValue();
            this.distanseMatrix[i+1][0] = pointsId[i].doubleValue();

            for (int j = i+1; j < pointIdLength; j++) {
                Vertex vertex1 = getVertexById(graph,pointsId[i]);
                Vertex vertex2 = getVertexById(graph,pointsId[j]);
                List<Path> paths = pathFinder.findShortestPaths(vertex1,vertex2,1);
                Path path = paths.get(0);
                this.pathList.add(path);
                this.distanseMatrix[i+1][j+1] = path.getTotalWeightForPath().getWeightValue();
                this.distanseMatrix[j+1][i+1] = this.distanseMatrix[i+1][j+1];
            }
        }
        this.distanseMatrix[pointIdLength][pointIdLength] = 0D;
    }








    public static void main(String[] args) {
        Road[] roads = {
                getRoad(getRoadId(1l, 2l), 4D),
                getRoad(getRoadId(1l, 5l), 1D),
                getRoad(getRoadId(2l, 3l), 2D),
                getRoad(getRoadId(2l, 5l), 2D),
                getRoad(getRoadId(2l, 6l), 1D),
                getRoad(getRoadId(3l, 6l), 2D),
                getRoad(getRoadId(3l, 4l), 2D),
                getRoad(getRoadId(4l, 7l), 3D)};

        Graph graph = createGraphFromRoads(roads);

        System.out.println(graph.getVertices());
        System.out.println(graph.getEdges());

        Point[] points = {getPoint(2L),
                getPoint(1L),
                getPoint(4L),
                getPoint(7L),
        };
        DistanceMatrix distanceMatrix = new DistanceMatrix(roads,points);

        Double[][] matrix = distanceMatrix.getDistanseMatrix();

        for(int i=0; i<matrix.length; i++){
            for (int j=0; j<matrix.length; j++){
                System.out.print(matrix[i][j]+"  ");
            }
            System.out.println();
        }

    }

    private static RoadId getRoadId(Long p1, Long p2) {
        return new RoadId(p1, p2);
    }

    private static Road getRoad(RoadId roadId, Double distance) {
        Road road = new Road();
        road.setId(roadId);
        road.setDistance(distance);
        return road;
    }

    private static Point getPoint(Long id){
        Point point = new Point();
        point.setId(id);
        return point;
    }


}
