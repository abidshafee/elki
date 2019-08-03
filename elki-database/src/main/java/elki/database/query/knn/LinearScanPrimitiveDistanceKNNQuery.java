/*
 * This file is part of ELKI:
 * Environment for Developing KDD-Applications Supported by Index-Structures
 *
 * Copyright (C) 2019
 * ELKI Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package elki.database.query.knn;

import elki.database.ids.*;
import elki.database.query.LinearScanQuery;
import elki.database.query.distance.PrimitiveDistanceQuery;
import elki.database.relation.Relation;
import elki.distance.PrimitiveDistance;

/**
 * Instance of this query for a particular database.
 * <p>
 * This is a subtle optimization: for primitive queries, it is clearly faster to
 * retrieve the query object from the relation only once!
 * 
 * @author Erich Schubert
 * @since 0.4.0
 * 
 * @assoc - - - PrimitiveDistanceQuery
 * @assoc - - - PrimitiveDistance
 */
public class LinearScanPrimitiveDistanceKNNQuery<O> extends AbstractDistanceKNNQuery<O> implements LinearScanQuery {
  /**
   * Unboxed distance function.
   */
  private PrimitiveDistance<? super O> rawdist;

  /**
   * Constructor.
   * 
   * @param distanceQuery Distance function to use
   */
  public LinearScanPrimitiveDistanceKNNQuery(PrimitiveDistanceQuery<O> distanceQuery) {
    super(distanceQuery);
    rawdist = distanceQuery.getDistance();
  }

  @Override
  public KNNList getKNNForDBID(DBIDRef id, int k) {
    final Relation<? extends O> relation = distanceQuery.getRelation();
    return linearScan(relation, relation.get(id), k);
  }

  @Override
  public KNNList getKNNForObject(O obj, int k) {
    return linearScan(distanceQuery.getRelation(), obj, k);
  }

  /**
   * Main loop of the linear scan.
   * 
   * @param relation Data relation
   * @param obj Query object
   * @param k Number of neighbors
   * @return Nearest neighbors
   */
  private KNNList linearScan(Relation<? extends O> relation, final O obj, int k) {
    final PrimitiveDistance<? super O> rawdist = this.rawdist;
    KNNHeap heap = DBIDUtil.newHeap(k);
    double max = Double.POSITIVE_INFINITY;
    for(DBIDIter iter = relation.iterDBIDs(); iter.valid(); iter.advance()) {
      final double dist = rawdist.distance(obj, relation.get(iter));
      max = dist <= max ? heap.insert(dist, iter) : max;
    }
    return heap.toKNNList();
  }
}
