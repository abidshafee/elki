package de.lmu.ifi.dbs.elki.database.ids;

import java.util.Comparator;

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

/**
 * Array-oriented implementation of a modifiable DBID collection.
 * 
 * @author Erich Schubert
 * 
 * @apiviz.has DBIDArrayMIter
 */
public interface ArrayModifiableDBIDs extends ModifiableDBIDs, ArrayDBIDs {
  /**
   * Sort the DBID set.
   */
  void sort();

  /**
   * Sort the DBID set.
   * 
   * @param comparator Comparator to use
   */
  void sort(Comparator<? super DBIDRef> comparator);

  /**
   * Sort the DBID set.
   * 
   * @param start Starting index, for partial sorting
   * @param end End index, for partial sorting (exclusive)
   * @param comparator Comparator to use
   */
  void sort(int start, int end, Comparator<? super DBIDRef> comparator);

  /**
   * Remove the i'th entry (starting at 0)
   * 
   * @param i Index
   * @return value removed
   */
  public DBID remove(int i);

  /**
   * Replace the i'th entry (starting at 0)
   * 
   * @param i Index
   * @param newval New value
   * @return previous value
   */
  public DBID set(int i, DBIDRef newval);

  /**
   * Swap DBIDs add positions a and b.
   * 
   * @param a First position
   * @param b Second position
   */
  public void swap(int a, int b);

  @Override
  public DBIDArrayMIter iter();
}
