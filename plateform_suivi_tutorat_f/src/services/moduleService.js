// services/moduleService.js

import axios from 'axios';
import sessionService from './sessionService';

const API_URL = `${process.env.REACT_APP_API_URL}/api/modules/`;


const createModule = async (data) => {
    try {
        const Token = sessionService.getSessionToken();
        const response = await axios.post(`${API_URL}create`,
            data, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la création du module:", error.response?.data?.message || error.message);
        throw error;
    }
};


const getAllModules = async () => {
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

const getModuleById = async (id) => {
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


const UpdateModules = async (data, id) => {
    try {

        const Token = sessionService.getSessionToken();

        const response = await axios.put(`${API_URL}update/${id}`,
            data,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Token}`
                }
            });

        return response.data;

    } catch (error) {
        console.error("Erreur lors du update: ", error.response?.data?.message || error.message);
        throw error;
    }
};

const deleteModule = async (id) => {
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

const ModuleService = {
    createModule,
    getAllModules,
    getModuleById,
    UpdateModules,
    deleteModule
};

export default ModuleService;