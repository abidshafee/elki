package de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar;

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

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.index.tree.spatial.SpatialEntry;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.AbstractRStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.bulk.BulkSplit;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.insert.InsertionStrategy;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.overflow.OverflowTreatment;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.split.SplitStrategy;
import de.lmu.ifi.dbs.elki.persistent.PageFile;

/**
 * Factory for regular R*-Trees.
 * 
 * @author Erich Schubert
 * 
 * @apiviz.landmark factory
 * @apiviz.uses RStarTreeIndex oneway - - «create»
 * 
 * @param <O> Object type
 */
public class RStarTreeFactory<O extends NumberVector<?>> extends AbstractRStarTreeFactory<O, RStarTreeNode, SpatialEntry, RStarTreeIndex<O>> {
  /**
   * Constructor.
   * 
   * @param fileName
   * @param pageSize
   * @param cacheSize
   * @param bulkSplitter Bulk loading strategy
   * @param insertionStrategy the strategy to find the insertion child
   * @param nodeSplitter the strategy for splitting nodes.
   * @param overflowTreatment the strategy to use for overflow treatment
   * @param minimumFill the relative minimum fill
   */
  public RStarTreeFactory(String fileName, int pageSize, long cacheSize, BulkSplit bulkSplitter, InsertionStrategy insertionStrategy, SplitStrategy nodeSplitter, OverflowTreatment overflowTreatment, double minimumFill) {
    super(fileName, pageSize, cacheSize, bulkSplitter, insertionStrategy, nodeSplitter, overflowTreatment, minimumFill);
  }

  @Override
  public RStarTreeIndex<O> instantiate(Relation<O> relation) {
    PageFile<RStarTreeNode> pagefile = makePageFile(getNodeClass());
    RStarTreeIndex<O> index = new RStarTreeIndex<>(relation, pagefile);
    index.setBulkStrategy(bulkSplitter);
    index.setInsertionStrategy(insertionStrategy);
    index.setNodeSplitStrategy(nodeSplitter);
    index.setOverflowTreatment(overflowTreatment);
    index.setMinimumFill(minimumFill);
    return index;
  }

  protected Class<RStarTreeNode> getNodeClass() {
    return RStarTreeNode.class;
  }

  /**
   * Parameterization class.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  public static class Parameterizer<O extends NumberVector<?>> extends AbstractRStarTreeFactory.Parameterizer<O> {
    @Override
    protected RStarTreeFactory<O> makeInstance() {
      return new RStarTreeFactory<>(fileName, pageSize, cacheSize, bulkSplitter, insertionStrategy, nodeSplitter, overflowTreatment, minimumFill);
    }
  }
}