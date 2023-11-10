package net.turtton.ktcakesample

interface MixinGenerateRandomGenerateUserID : UseGenerateUserID {
    override val generateUserID: GenerateUserID
        get() = RandomGenerateUserID
}

private object RandomGenerateUserID : GenerateUserID {
    override fun generate(): UserID = UserID(java.util.UUID.randomUUID().toString())
}

interface MixinRegisterUserWorkflow : UseRegisterUserWorkflow {
    override val registerUserWorkflow: RegisterGenerateUserWorkflow
        get() = RegisterWorkflowImpl
}

private object RegisterWorkflowImpl : RegisterGenerateUserWorkflow, MixinGenerateRandomGenerateUserID, MixinUserRepository

interface MixinChangeDisplayNameUserWorkflow : UseChangeDisplayNameUserWorkflow {
    override val changeDisplayNameUserWorkflow: ChangeDisplayNameUserWorkflow
        get() = ChangeDisplayNameUserWorkflowImpl
}

private object ChangeDisplayNameUserWorkflowImpl : ChangeDisplayNameUserWorkflow, MixinUserRepository

interface MixinDumpAllUserWorkflow : UseDumpAllUserWorkflow {
    override val dumpAllUserWorkflow: DumpAllUserWorkflow
        get() = DumpAllUserWorkflowImpl
}

private object DumpAllUserWorkflowImpl : DumpAllUserWorkflow, MixinUserRepository

interface MixinUserRepository : UseUserRepository {
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
