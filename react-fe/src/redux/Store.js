import { configureStore } from '@reduxjs/toolkit';
import authSlice from './slices/AuthSlice';

const store = configureStore({
  reducer: {
    auth: authSlice,
  },
});

export default store;
