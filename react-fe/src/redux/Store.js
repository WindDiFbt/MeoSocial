import { configureStore } from '@reduxjs/toolkit';
import authSlice from './slices/AuthSlice';
import postSlice from './slices/PostSlice';

const store = configureStore({
  reducer: {
    auth: authSlice,
    posts: postSlice,
  },
});

export default store;
