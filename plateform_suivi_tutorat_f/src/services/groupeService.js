// services/groupeService.js

import axios from 'axios';
import sessionService from './sessionService';

const API_URL = `${process.env.REACT_APP_API_URL}/api/groupes/`;


const createGroupe = async (data) => {
    try {
        const Token = sessionService.getSessionToken();
        const response = await axios.post(`${API_URL}create`,
            {
                nom: data.nom,
                module: {
                    id: data.module.id
                }
            }, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la création du Groupe:", error.response?.data?.message || error.message);
        throw error;
    }
};


const getAllGroupes = async () => {
    try {

        const Token = sessionService.getSessionToken();

        const response = await axios.get(`${API_URL}all`, {
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

const getGroupeById = async (id) => {
    try {

        const Token = sessionService.getSessionToken();

        const response = await axios.get(`${API_URL}${id}`, {
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

const UpdateGroupes = async (data, id) => {
    try {
        const Token = sessionService.getSessionToken();
        const response = await axios.put(`${API_URL}update/${id}`,
            {
                nom: data.nom,
                module: {
                    id: data.module.id
                }
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Token}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la mise à jour du groupe:", error.response?.data?.message || error.message);
        throw error;
    }
};

const deleteGroupe = async (id) => {
    try {

        const Token = sessionService.getSessionToken();

        const response = await axios.delete(`${API_URL}delete/${id}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });

        return response.data;
    } catch (error) {
        console.error("Erreur lors de la suppression:", error.response?.data?.message || error.message);
        throw error;

    }
};

const GroupeService = {
    createGroupe,
    getAllGroupes,
    getGroupeById,
    UpdateGroupes,
    deleteGroupe
};

export default GroupeService;