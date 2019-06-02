/**
 * Copyright (C) 2019 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.easy.properties.app.model

import nl.knaw.dans.easy.properties.app.model.DoiAction.DoiAction
import nl.knaw.dans.easy.properties.app.model.SeriesFilter.SeriesFilter

sealed abstract class DoiEvent[T](value: T, timestamp: Timestamp)

case class DoiRegisteredEvent(value: Boolean, timestamp: Timestamp) extends DoiEvent(value, timestamp)
case class DoiActionEvent(value: DoiAction, timestamp: Timestamp) extends DoiEvent(value, timestamp)

case class DepositDoiRegisteredFilter(value: Boolean, filter: SeriesFilter = SeriesFilter.LATEST) extends DepositFilter
case class DepositDoiActionFilter(value: DoiAction, filter: SeriesFilter = SeriesFilter.LATEST) extends DepositFilter

object DoiAction extends Enumeration {
  type DoiAction = Value

  // @formatter:off
  val CREATE: DoiAction = Value("CREATE")
  val UPDATE: DoiAction = Value("UPDATE")
  val NONE:   DoiAction = Value("NONE")
  // @formatter:on
}