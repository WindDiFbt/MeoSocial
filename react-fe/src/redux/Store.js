import { configureStore } from '@reduxjs/toolkit';
import authSlice from './slices/AuthSlice';

const store = configureStore({
  reducer: {
    counter: counterSlice,
    auth: authSlice,
  },
});

export default store;
