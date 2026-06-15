package com.nastia.catalogapp.usecase

import com.nastia.catalogapp.model.AuthError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateLoginInputUseCaseTest {

    private val useCase = ValidateLoginInputUseCase()

    @Test
    fun `blank username returns USERNAME_REQUIRED`() {
        val result = useCase("", "password123")
        assertEquals(AuthError.USERNAME_REQUIRED, result.usernameError)
    }

    @Test
    fun `blank password returns PASSWORD_REQUIRED`() {
        val result = useCase("testuser", "")
        assertEquals(AuthError.PASSWORD_REQUIRED, result.passwordError)
    }

    @Test
    fun `short password returns PASSWORD_TOO_SHORT`() {
        val result = useCase("testuser", "123")
        assertEquals(AuthError.PASSWORD_TOO_SHORT, result.passwordError)
    }

    @Test
    fun `valid input returns no errors`() {
        val result = useCase("testuser", "password123")
        assertNull(result.usernameError)
        assertNull(result.passwordError)
        assertTrue(result.isValid)
    }
}