package net.turtton.ktcakesample

fun main(args: Array<String>) {
    val env =
        object :
            Environment,
//        MixinUserRepository,
            MixinInMemoryRegisterUserWorkflow,
            MixinInMemoryChangeDisplayNameUserWorkflow,
            MixinInMemoryDumpAllUserWorkflow
//        MixinGenerateRandomUserId
        {}

    env.exec()
}

// 以下はリンク先のコードと同等
// https://github.com/RayStarkMC/kotlin-cake-pattern-sample/blob/50f4bafe35d7c6ffe5dec4417261932dd10164b5/src/main/kotlin/net/raystarkmc/ktcakesample/Main.kt#L16-L37
interface Environment :
    UseRegisterUserWorkflow,
    UseChangeDisplayNameUserWorkflow,
    UseDumpAllUserWorkflow

fun Environment.exec() {
    val registerUserForm =
        RegisterUserForm(
            displayName = UserDisplayName("turtton"),
            mailAddress = UserMailAddress("turtton@example.com"),
        )
    val id = registerUserWorkflow.register(registerUserForm)

    dumpAllUserWorkflow.dumpAll().forEach(::println)

    val changeDisplayNameForm =
        ChangeDisplayNameForm(
            id = id,
            displayName = UserDisplayName("わたがめ"),
        )
    changeDisplayNameUserWorkflow.changeDisplayName(changeDisplayNameForm)

    dumpAllUserWorkflow.dumpAll().forEach(::println)
}
