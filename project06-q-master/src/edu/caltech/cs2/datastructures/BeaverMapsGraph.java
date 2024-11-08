package edu.caltech.cs2.datastructures;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();
        JsonElement builds = fromFile(buildingsFileName);
        JsonElement waypoints = fromFile(waypointsFileName);
        JsonElement roads = fromFile(roadsFileName);
        for (JsonElement build : builds.getAsJsonArray()){
            Location l = new Location(build.getAsJsonObject());
            this.buildings.add(l);
            this.addVertex(l.id);
            this.ids.put(l.id, l);
        }

        for (JsonElement way : waypoints.getAsJsonArray()) {
            Location l = new Location(way.getAsJsonObject());
            this.addVertex(l);
            this.ids.put(l.id, l);
        }

        for (JsonElement road : roads.getAsJsonArray()) {
            Long prev = null;
            for (JsonElement r2 : road.getAsJsonArray()) {
                Location v2 = this.getLocationByID(r2.getAsLong());
                Long curr = v2.id;
                if (prev != null) {
                    Location v1 = this.ids.get(prev);
                    this.addUndirectedEdge(prev, curr, v1.getDistance(v2));
                }
                prev = curr;
            }
        }

    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> locs = new LinkedDeque<>();
        for (Location loc : this.ids.values()){
            if (loc.name != null && loc.name.equals(locName)){
                locs.add(loc);
            }
        }
        return locs;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        return this.ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        if (this.ids.containsKey(n.id)){
            return false;
        }
        this.addVertex(n.id);
        this.ids.put(n.id, n);
        return true;
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        Location closest = null;
        double min = -1;
        for (Location build : this.buildings){
            double dist = build.getDistance(lat, lon);
            if (closest == null || dist == -1 || dist < min){
                min = dist;
                closest = build;
            }
        }
        return closest;
    }

    /**
     * Returns a set of locations which are reachable along a path that goes no further than `threshold` feet from start
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return all locations within the provided `threshold` feet from start
     */
    public ISet<Location> dfs(Location start, double threshold) {
        Location current_loc = start;
        IDeque<Location> unvisited = new LinkedDeque<>();
        ISet<Location> reachable = new ChainingHashSet<>();
        ISet<Location> visited = new ChainingHashSet<>();
        while (true) {
            Long current_id = current_loc.id;
            visited.add(current_loc);
            reachable.add(current_loc);
            for (Long id : this.neighbors(current_id)) {
                Location n = this.ids.get(id);
                if (threshold >= n.getDistance(start) && !visited.contains(n)) {
                    unvisited.add(n);
                }
            }
            if (unvisited.isEmpty()) {
                break;
            }
            current_loc = unvisited.removeFront();
        }
        return reachable;
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {
        // TODO (student): Write This
        return null;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            return JsonParser.parseReader(reader);
        } catch (IOException e) {
            return null;
        }
    }
}
