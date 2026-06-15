package com.nastia.catalogapp.auth

import com.nastia.catalogapp.model.AuthError
import com.nastia.catalogapp.usecase.ValidateLoginInputUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(fakeRepo: FakeAuthRepository = FakeAuthRepository()): AuthViewModel {
        return AuthViewModel(fakeRepo, ValidateLoginInputUseCase())
    }

    @Test
    fun `blank username sets USERNAME_REQUIRED error and does not call repository`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val viewModel = createViewModel(fakeRepo)

        viewModel.onPasswordChange("password123")
        viewModel.login()

        assertEquals(AuthError.USERNAME_REQUIRED, viewModel.uiState.value.usernameError)
        assertEquals(0, fakeRepo.loginCallCount)
    }

    @Test
    fun `valid credentials result in loginSuccess true`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val viewModel = createViewModel(fakeRepo)

        viewModel.onUsernameChange("testuser")
        viewModel.onPasswordChange("password123")
        viewModel.login()

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loginSuccess)
        assertEquals(1, fakeRepo.loginCallCount)
    }

    @Test
    fun `invalid credentials set formError INVALID_CREDENTIALS`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val viewModel = createViewModel(fakeRepo)

        viewModel.onUsernameChange("testuser")
        viewModel.onPasswordChange("wrongpassword")
        viewModel.login()

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(AuthError.INVALID_CREDENTIALS, viewModel.uiState.value.formError)
        assertFalse(viewModel.uiState.value.loginSuccess)
    }

    @Test
    fun `onUsernameChange clears username error and form error`() = runTest {
        val fakeRepo = FakeAuthRepository()
        val viewModel = createViewModel(fakeRepo)

        // trigger an error first
        viewModel.login()
        assertEquals(AuthError.USERNAME_REQUIRED, viewModel.uiState.value.usernameError)

        viewModel.onUsernameChange("testuser")

        assertEquals(null, viewModel.uiState.value.usernameError)
    }
}