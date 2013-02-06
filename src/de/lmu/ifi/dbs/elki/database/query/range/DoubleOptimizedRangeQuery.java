package de.lmu.ifi.dbs.elki.database.query.range;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDRef;
import de.lmu.ifi.dbs.elki.database.ids.distance.DistanceDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.distance.ModifiableDoubleDistanceDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.integer.DoubleDistanceIntegerDBIDKNNList;
import de.lmu.ifi.dbs.elki.database.query.LinearScanQuery;
import de.lmu.ifi.dbs.elki.database.query.distance.DistanceQuery;
import de.lmu.ifi.dbs.elki.distance.distancefunction.PrimitiveDoubleDistanceFunction;
import de.lmu.ifi.dbs.elki.distance.distancevalue.DoubleDistance;

/**
 * Default linear scan range query class.
 * 
 * @author Erich Schubert
 * 
 * @apiviz.uses PrimitiveDoubleDistanceFunction
 * 
 * @param <O> Database object type
 */
public class DoubleOptimizedRangeQuery<O> extends LinearScanRangeQuery<O, DoubleDistance> implements LinearScanQuery {
  /**
   * Raw distance function.
   */
  PrimitiveDoubleDistanceFunction<O> rawdist;

  /**
   * Constructor.
   * 
   * @param distanceQuery Distance function to use
   */
  @SuppressWarnings("unchecked")
  public DoubleOptimizedRangeQuery(DistanceQuery<O, DoubleDistance> distanceQuery) {
    super(distanceQuery);
    if (!(distanceQuery.getDistanceFunction() instanceof PrimitiveDoubleDistanceFunction)) {
      throw new UnsupportedOperationException("DoubleOptimizedRangeQuery instantiated for non-PrimitiveDoubleDistanceFunction!");
    }
    rawdist = (PrimitiveDoubleDistanceFunction<O>) distanceQuery.getDistanceFunction();
  }

  @Override
  public DistanceDBIDList<DoubleDistance> getRangeForDBID(DBIDRef id, DoubleDistance range) {
    double epsilon = range.doubleValue();

    O qo = relation.get(id);
    ModifiableDoubleDistanceDBIDList result = new DoubleDistanceIntegerDBIDKNNList();
    for (DBIDIter iter = relation.getDBIDs().iter(); iter.valid(); iter.advance()) {
      double doubleDistance = rawdist.doubleDistance(qo, relation.get(iter));
      if (doubleDistance <= epsilon) {
        result.add(doubleDistance, iter);
      }
    }
    result.sort();
    return result;
  }

  @Override
  public DistanceDBIDList<DoubleDistance> getRangeForObject(O obj, DoubleDistance range) {
    double epsilon = range.doubleValue();

    ModifiableDoubleDistanceDBIDList result = new DoubleDistanceIntegerDBIDKNNList();
    for (DBIDIter iter = relation.getDBIDs().iter(); iter.valid(); iter.advance()) {
      double doubleDistance = rawdist.doubleDistance(obj, relation.get(iter));
      if (doubleDistance <= epsilon) {
        result.add(doubleDistance, iter);
      }
    }
    result.sort();
    return result;
  }
}
