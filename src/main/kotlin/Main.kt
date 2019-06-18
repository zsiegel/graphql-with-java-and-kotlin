import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import java.nio.charset.Charset


object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        //Load the schema from the resources folder
        val schemaStream = this.javaClass.classLoader.getResourceAsStream("graphql/schema.graphql")

        val typeRegistry = schemaStream.use { stream ->

            //Read the bytes into a string
            val schemaString = String(stream.readBytes(), Charset.forName("UTF-8"))

            //Create and return a TypeRegistry
            SchemaParser().parse(schemaString)
        }

        val runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring()

        val queryType = TypeRuntimeWiring.newTypeWiring("Query")
        queryType.dataFetcher("users", UsersFetcher())

        runtimeWiringBuilder.type(queryType)

        val runtimeWiring = runtimeWiringBuilder.build()

        val executableSchema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)

        val graphQLSchema = GraphQL.newGraphQL(executableSchema).build()

        val usersQuery = """
                query {
                    users {
                        id
                        name
                        email
                    }
                }
            """.trimIndent()

        val result = graphQLSchema.execute(usersQuery)
        println("Result: ${result.getData<Map<String, String>>()}")

        result.getData<Map<String, List<Object>>>()["users"]?.forEach { item ->
            println(item)
        }

    }
}