import Cookies from 'js-cookie';
import AuthService from './authService';

// Fonction pour vérifier si l'utilisateur est authentifié
const isAuthenticated = () => {
    const token = Cookies.get('userToken');
    // Retourne true si le token existe, sinon false
    return !!token;
};

// Fonction pour obtenir le token de session
const getSessionToken = () => {
    return Cookies.get('userToken');
};

// Fonction pour vérifier si la session est encore valide en appelant l'API
const isSessionValid = async () => {
    const token = getSessionToken();

    if (!token) {
        return false; // Pas de token, session invalide
    }

    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/auth/validate-token?token=${token}`);
        const data = await response.json();

        if (response.ok && data.message === 'Token valide') {
            return true; // Token valide
        } else {
            AuthService.logout();
            return false; // Token invalide
        }
    } catch (error) {
        console.error('Erreur lors de la vérification de la session:', error);
        return false; // Erreur lors de la vérification
    }
};

const userInformation = async () => {
    const token = getSessionToken();

    if (!token) {
        return false; // Pas de token, session invalide
    }

    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/auth/validate-token?token=${token}`);
        const data = await response.json();

        if (response.ok && data.message === 'Token valide') {
            return data; // Token valide
        } 
    } catch (error) {
        console.error('Erreur lors de la vérification de la session:', error);
        return null; // Erreur lors de la vérification
    }
};

// Fonction pour démarrer la session
const startSession = async (email, password) => {
    const response = await AuthService.login(email, password);
    return response;
};

// Fonction pour terminer la session
const endSession = () => {
    AuthService.logout();
};

const sessionService = {
    isAuthenticated,
    getSessionToken,
    isSessionValid,
    startSession,
    endSession,
    userInformation
};

export default sessionService;
