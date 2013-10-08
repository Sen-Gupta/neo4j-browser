/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.pipes

import org.neo4j.cypher.internal.commands.Predicate
import org.neo4j.cypher.internal.symbols.SymbolTable
import org.neo4j.cypher.internal.data.SimpleVal
import org.neo4j.cypher.internal.ExecutionContext

class FilterPipe(source: Pipe, val predicate: Predicate) extends PipeWithSource(source) {
  val symbols = source.symbols

  protected def internalCreateResults(input: Iterator[ExecutionContext],state: QueryState) =
    input.filter(ctx => predicate.isMatch(ctx)(state))

  override def executionPlanDescription =
    source.executionPlanDescription.andThen(this, "Filter", "pred" -> SimpleVal.fromStr(predicate))

  def throwIfSymbolsMissing(symbols: SymbolTable) {
    predicate.throwIfSymbolsMissing(symbols)
  }
}