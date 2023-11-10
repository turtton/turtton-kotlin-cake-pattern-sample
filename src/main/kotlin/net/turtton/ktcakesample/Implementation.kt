package net.turtton.ktcakesample

interface MixinRandomGenerateUserID : UseGenerateUserID {
    override val generateUserID: GenerateUserID
        get() = RandomGenerateUserID
}

private object RandomGenerateUserID : GenerateUserID {
    override fun generate(): UserID = UserID(java.util.UUID.randomUUID().toString())
}

interface MixinInMemoryRegisterUserWorkflow : UseRegisterUserWorkflow {
    override val registerUserWorkflow: RegisterGenerateUserWorkflow
        get() = InMemoryRegisterWorkflow
}

private object InMemoryRegisterWorkflow : RegisterGenerateUserWorkflow, MixinRandomGenerateUserID, MixinInMemoryUserRepository

interface MixinInMemoryChangeDisplayNameUserWorkflow : UseChangeDisplayNameUserWorkflow {
    override val changeDisplayNameUserWorkflow: ChangeDisplayNameUserWorkflow
        get() = InMemoryChangeDisplayNameUserWorkflow
}

private object InMemoryChangeDisplayNameUserWorkflow : ChangeDisplayNameUserWorkflow, MixinInMemoryUserRepository

interface MixinInMemoryDumpAllUserWorkflow : UseDumpAllUserWorkflow {
    override val dumpAllUserWorkflow: DumpAllUserWorkflow
        get() = InMemoryDumpAllUserWorkflow
}

private object InMemoryDumpAllUserWorkflow : DumpAllUserWorkflow, MixinInMemoryUserRepository

interface MixinInMemoryUserRepository : UseUserRepository {
    override val userRepository: UserRepository
        get() = InMemoryUserRepository
}

// objectなので単一のインスタンスしか持てない
// ただ、このようなRepositoryの実装は大抵DBとのやりとりになるので特に問題にならなかった
private object InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UserID, User>()

    override fun loadByID(id: UserID): User? = users[id]

    override fun loadByMailAddress(mailAddress: UserMailAddress): User? = users.values.find { it.mailAddress == mailAddress }

    override fun loadAllUsers(): List<User> = users.values.toList()

    override fun save(user: User) {
        users[user.id] = user
    }
}
