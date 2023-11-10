package net.turtton.ktcakesample

data class RegisterUserForm(
    val displayName: UserDisplayName,
    val mailAddress: UserMailAddress,
)

interface UseRegisterUserWorkflow {
    val registerUserWorkflow: RegisterGenerateUserWorkflow
}

// 実装前のinterfaceの段階で使用する依存関係を明記しておく
// この段階で複数の実装を持たない場合は詳細が書ける
interface RegisterGenerateUserWorkflow : UseGenerateUserID, UseUserRepository {
    fun register(workflow: RegisterUserForm): UserID {
        userRepository.loadByMailAddress(workflow.mailAddress)?.apply {
            throw IllegalStateException("ユーザーは登録済み")
        }

        val userID = generateUserID.generate()
        val user =
            User(
                userID,
                workflow.displayName,
                workflow.mailAddress,
            )
        userRepository.save(user)
        return userID
    }
}

data class ChangeDisplayNameForm(
    val id: UserID,
    val displayName: UserDisplayName,
)

interface UseChangeDisplayNameUserWorkflow {
    val changeDisplayNameUserWorkflow: ChangeDisplayNameUserWorkflow
}

interface ChangeDisplayNameUserWorkflow : UseUserRepository {
    fun changeDisplayName(workflow: ChangeDisplayNameForm) {
        val user =
            userRepository.loadByID(workflow.id)
                ?: throw IllegalStateException("ユーザーが存在しない")
        val modifiedUser = user.copy(displayName = workflow.displayName)
        userRepository.save(modifiedUser)
    }
}

interface UseDumpAllUserWorkflow {
    val dumpAllUserWorkflow: DumpAllUserWorkflow
}

interface DumpAllUserWorkflow : UseUserRepository {
    fun dumpAll(): List<String> =
        userRepository.loadAllUsers().map {
            "${it.id.unwrap} ${it.displayName.unwrap} ${it.mailAddress.unwrap}"
        }
}
