package ru.example.zencartest.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.example.zencartest.model.UserModel
import java.time.OffsetDateTime

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val birthDate: String,
    val password: String,
    val registrationDate: String
) {
    fun toModel() = UserModel(
        id = id,
        login = login,
        birthDate = OffsetDateTime.parse(birthDate),
        password = password,
        registrationDate = OffsetDateTime.parse(registrationDate)
    )

    companion object {
        fun toEntity(userModel: UserModel) = UserEntity(
            id = userModel.id,
            login = userModel.login,
            birthDate = userModel.birthDate.toString(),
            password = userModel.password,
            registrationDate = userModel.registrationDate.toString()
        )
    }
}