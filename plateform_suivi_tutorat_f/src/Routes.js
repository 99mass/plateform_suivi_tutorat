// src/Routes.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import AdminTrackerDashboardPage from './pages/AdminTrackerDashboardPage';
import TuteurDashboardPage from './pages/TuteurDashboardPage';

import NotFoundPage from './pages/NotFoundPage';
import ProfilePage from './pages/ProfilePage';
import PrivateRoute from './components/PrivateRoute';

const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />

                <Route path="/dasboard" element={<PrivateRoute element={<AdminTrackerDashboardPage />} path={"/dasboard"} />} />
                <Route path="/tuteur" element={<PrivateRoute element={<TuteurDashboardPage />} path={"/tuteur"} />} />
                <Route path="/profile" element={<PrivateRoute element={<ProfilePage />} path={"/profile"} />} />

                <Route path="/404" element={<NotFoundPage />} />
                <Route path="*" element={<Navigate to="/404" replace />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;
