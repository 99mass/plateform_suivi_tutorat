import axios from 'axios';
import Cookies from 'js-cookie';
import sessionService from './sessionService';

const API_URL = `${process.env.REACT_APP_API_URL}/api/auth/login`;

const login = async (email, password) => {
  try {

    const response = await axios.post(API_URL, {
      email: email,
      motDePasse: password,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (response.data.token) {


      // Stocke le token JWT dans un cookie
      Cookies.set('userToken', response.data.token, { expires: 7, secure: true, sameSite: 'Strict' });
    }

    return response.data;  // Retourne les données de l'utilisateur
  } catch (error) {
    console.error("Erreur lors de la connexion:", error);
    throw error;
  }
};

const UpdateUser = async (data, id) => {
  try {

    const Token = sessionService.getSessionToken();

   await axios.put(`${process.env.REACT_APP_API_URL}/api/users/update-user/${id}`,
      data,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Token}`
        }
      });


  } catch (error) {
    console.error("Erreur lors du update:", error.message);
  }
};

const logout = () => {
  Cookies.remove('userToken');
};

const AuthService = {
  login,
  logout,
  UpdateUser
};

export default AuthService;
