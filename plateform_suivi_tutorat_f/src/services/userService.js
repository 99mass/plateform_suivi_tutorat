// services/userService.js

import axios from 'axios';
import sessionService from './sessionService';

const API_URL = `${process.env.REACT_APP_API_URL}/api/users/`;


const createUser = async (data) => {
    try {
        const Token = sessionService.getSessionToken();

        await axios.post(`${API_URL}create-user`,
            data, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });

    } catch (error) {
        console.error("Erreur lors de la creation:", error.message);
        throw error;
    }
};

const listUsers = async (role) => {
    try {

        const Token = sessionService.getSessionToken();
        let path = "all";
        switch (role) {
            case "Administrateur": path = "admins"; break;
            case "Tracker": path = "trackers"; break;
            default: path = "tuteurs"; break;
        }

        const response = await axios.get(`${API_URL}${path}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });


        return response.data;

    } catch (error) {
        console.error("Erreur lors de la recuperation:", error.message);
        return [];
    }
};


const deleteUsers = async (id) => {
    try {

        const Token = sessionService.getSessionToken();

        await axios.delete(`${API_URL}detete-user/${id}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });

    } catch (error) {
        console.error("Erreur lors de la suppression:", error.message);

    }
};


const UserService = {
    createUser,
    listUsers,
    deleteUsers
};

export default UserService;