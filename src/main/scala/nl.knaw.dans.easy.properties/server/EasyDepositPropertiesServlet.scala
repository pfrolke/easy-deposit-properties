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
package nl.knaw.dans.easy.properties.server

import nl.knaw.dans.easy.properties.EasyDepositPropertiesApp
import nl.knaw.dans.lib.logging.DebugEnhancedLogging
import nl.knaw.dans.lib.logging.servlet._
import org.scalatra._

class EasyDepositPropertiesServlet(app: EasyDepositPropertiesApp,
                                   version: String) extends ScalatraServlet
  with ServletLogger
  with PlainLogFormatter
  with LogResponseBodyOnError
  with DebugEnhancedLogging {

  get("/") {
    contentType = "text/plain"
    Ok(s"EASY Deposit Properties Service running ($version)")
  }
}
