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
package nl.knaw.dans.easy.properties.app.graphql.types

import nl.knaw.dans.easy.properties.app.graphql.DataContext
import nl.knaw.dans.easy.properties.app.graphql.ordering.{ OrderDirection, SpringfieldOrder, SpringfieldOrderField }
import nl.knaw.dans.easy.properties.app.graphql.resolvers.SpringfieldResolver
import nl.knaw.dans.easy.properties.app.model.Deposit
import nl.knaw.dans.easy.properties.app.model.springfield.{ Springfield, SpringfieldPlayMode }
import sangria.macros.derive._
import sangria.marshalling.FromInput
import sangria.marshalling.FromInput._
import sangria.relay._
import sangria.schema.{ Argument, Context, DeferredValue, EnumType, Field, InputObjectType, ObjectType, OptionInputType, OptionType }

trait SpringfieldType {
  this: DepositType
    with NodeType
    with MetaTypes
    with Scalars =>

  implicit val SpringfieldPlayModeType: EnumType[SpringfieldPlayMode.Value] = deriveEnumType(
    EnumTypeDescription("The playmode in Springfield for this deposit."),
    DocumentValue("CONTINUOUS", "Play audio/video continuously in Springfield."),
    DocumentValue("MENU", "Play audio/video in Springfield as selected in a menu."),
  )

  private val depositField: Field[DataContext, Springfield] = Field(
    name = "deposit",
    description = Some("Returns the deposit that is associated with this particular springfield configuration."),
    fieldType = OptionType(DepositType),
    resolve = getDepositBySpringfield(_),
  )

  private def getDepositBySpringfield(implicit context: Context[DataContext, Springfield]): DeferredValue[DataContext, Option[Deposit]] = {
    SpringfieldResolver.depositBySpringfieldId(context.value.id)
  }

  implicit val SpringfieldType: ObjectType[DataContext, Springfield] = deriveObjectType(
    ObjectTypeDescription("Springfield configuration associated with this deposit."),
    Interfaces[DataContext, Springfield](nodeInterface),
    DocumentField("domain", "The domain of Springfield."),
    DocumentField("user", "The user of Springfield."),
    DocumentField("collection", "The collection of Springfield."),
    DocumentField("playmode", "The playmode used in Springfield."),
    DocumentField("timestamp", "The timestamp at which this springfield configuration was associated with the deposit."),
    AddFields(
      depositField,
    ),
    ReplaceField("id", Node.globalIdField[DataContext, Springfield]),
  )

  implicit val SpringfieldOrderFieldType: EnumType[SpringfieldOrderField.Value] = deriveEnumType()
  implicit val SpringfieldOrderInputType: InputObjectType[SpringfieldOrder] = deriveInputObjectType(
    InputObjectTypeDescription("Ordering options for springfields"),
    DocumentInputField("field", "The field to order springfields by"),
    DocumentInputField("direction", "The ordering direction"),
  )
  implicit val SpringfieldOrderFromInput: FromInput[SpringfieldOrder] = fromInput(ad => SpringfieldOrder(
    field = ad("field").asInstanceOf[SpringfieldOrderField.Value],
    direction = ad("direction").asInstanceOf[OrderDirection.Value],
  ))
  val optSpringfieldOrderArgument: Argument[Option[SpringfieldOrder]] = {
    Argument(
      name = "orderBy",
      argumentType = OptionInputType(SpringfieldOrderInputType),
      description = Some("Ordering options for the returned springfields."),
      defaultValue = None,
      fromInput = optionInput(inputObjectResultInput(SpringfieldOrderFromInput)),
      astDirectives = Vector.empty,
      astNodes = Vector.empty,
    )
  }
}
