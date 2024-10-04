// src/pages/LoginPage.js
import React, { useState } from 'react';
import '../styles/LoginPage.css';
import { IconButton, InputLabel, OutlinedInput, TextField, InputAdornment, FormControl } from '@mui/material';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import { Link, useNavigate } from 'react-router-dom';
import sessionService from '../services/sessionService.js';


const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();


  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };

  const handleLogin = async (event) => {
    event.preventDefault();
    setErrorMessage(''); // Réinitialiser les erreurs

    try {
      // Appel du service AuthService pour la connexion
      const response = await sessionService.startSession(email, password);
    

      switch (response.role) {
        case "admin": navigate('/dasboard');
          break;
        case "tracker": navigate('/dasboard');
          break;
        case "tuteur": navigate('/tuteur');
          break;

        default: navigate('/');
          break;
      }


    } catch (error) {
      setErrorMessage('Email ou mot de passe incorrect.');
    }
  };

  return (
    <div className='main-login'>
      <div className="login-page">
        <h1>Se connecter</h1>
        {errorMessage && <div className="error-message">{errorMessage}</div>}
        <form onSubmit={handleLogin}>
          <TextField
            type='email'
            id="email"
            label="Email"
            variant="outlined"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <FormControl variant="outlined">
            <InputLabel htmlFor="outlined-adornment-password">Mot de passe</InputLabel>
            <OutlinedInput
              id="outlined-adornment-password"
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                    className='icon-eyes'
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
              label="Mot de passe"
              required
            />
          </FormControl>
          <button type="submit">Se connecter</button>
        </form>
        <Link className='link-href' to={"/home"} >Mot de passe oublié ?</Link>
      </div>
    </div>
  );
};

export default LoginPage;
