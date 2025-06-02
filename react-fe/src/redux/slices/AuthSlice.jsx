import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    user: null,
    isLoggedIn: false,
    emailForVerification: null,
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        login: (state, action) => {
            state.user = action.payload;
            state.isLoggedIn = true;
        },
        logout: (state) => {
            state.user = null;
            state.isLoggedIn = false;
        },
        setEmailForVerification: (state, action) => {
            state.emailForVerification = action.payload;
        },
        clearEmailForVerification: (state) => {
            state.emailForVerification = null;
        },
    },
});

export const {
    login,
    logout,
    setEmailForVerification,
    clearEmailForVerification
} = authSlice.actions;
export default authSlice.reducer;