// services/seancesService.js

import axios from 'axios';
import sessionService from './sessionService';

const API_URL = `${process.env.REACT_APP_API_URL}/api/seances/`;

const createSeance = async (tuteurId, moduleId, groupeId) => {
    try {
        const Token = sessionService.getSessionToken();
        const response = await axios.post(`${API_URL}create`, null, {
            params: {
                tuteurId,
                moduleId,
                groupeId
            },
            headers: {
                'Authorization': `Bearer ${Token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la création de la Séance:", error.response?.data?.message || error.message);
        throw error;
    }
};


const getAllSeances = async () => {
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

const getSeanceById = async (id) => {
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

const getSeancesByTuteurId = async (id) => {
    try {

        const Token = sessionService.getSessionToken();

        const response = await axios.get(`${API_URL}tuteur/${id}`, {
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

const markedSeanceStatus = async (id, effectuee) => {
    try {
        const Token = sessionService.getSessionToken();
        const response = await axios.put(`${API_URL}marked/${id}`, null, {
            params: {
                effectuee
            },

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

const updateSeance = async (data, id) => {
    try {
        const token = sessionService.getSessionToken();
        const response = await axios.put(
            `${API_URL}update/${id}`, 
            data, 
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la mise à jour de la séance:", error.response?.data?.message || error.message);
        throw error;
    }
};



const deleteSeance = async (id) => {
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

const SeanceService = {
    createSeance,
    getAllSeances,
    getSeanceById,
    getSeancesByTuteurId,
    markedSeanceStatus,
    deleteSeance,
    updateSeance
};

export default SeanceService;