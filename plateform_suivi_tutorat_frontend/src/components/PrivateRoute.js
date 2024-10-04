// src/components/PrivateRoute.js
import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { CircularProgress, Box } from '@mui/material';
import sessionService from '../services/sessionService';

const PrivateRoute = ({ element, path }) => {
    const [isValid, setIsValid] = useState(false);
    const [Role, setRole] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkSession = async () => {
            const valid = await sessionService.isSessionValid();
            setIsValid(valid);

            const infos = await sessionService.userInformation();
            setRole(infos.role);
            setLoading(false);
        };

        checkSession();
    }, []);

    if (loading) {
        return (
            <Box
                sx={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh'
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    if (!isValid) {
        return <Navigate to="/" />;
    }
    if (path === "/profile") {
        return element;
    }
    if ((Role === "admin" || Role === "tracker") && path !== "/dasboard") {
        return <Navigate to="/" />;
    }
    if (Role === "tuteur" && path !== "/tuteur") {
        return <Navigate to="/" />;
    }

    return element;
};

export default PrivateRoute;
