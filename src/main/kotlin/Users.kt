import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import java.util.*

data class User(val id: UUID, val name: String, val email: String)

internal val ALL_USERS = listOf(
    User(UUID.randomUUID(), "Bob", "bob@graphql.com"),
    User(UUID.randomUUID(), "Jane", "jane@graphql.com"),
    User(UUID.randomUUID(), "Mary", "mary@graphql.com")
)

class UsersFetcher : DataFetcher<List<User>> {
    override fun get(environment: DataFetchingEnvironment?): List<User> {
        return ALL_USERS
    }
}