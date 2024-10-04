// src/pages/NotFoundPage.js
import React from 'react'
import { Button, Container, Typography, Box } from '@mui/material'
import { Home as HomeIcon } from '@mui/icons-material'
import { useNavigate } from 'react-router-dom'

export default function NotFoundPage() {
  const navigate = useNavigate()

  const handleReturn = () => {
    navigate('/')
  }

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100vh',
          textAlign: 'center',
        }}
      >
        <Typography
          variant="h1"
          component="h1"
          gutterBottom
          sx={{
            fontSize: '8rem',
            fontWeight: 'bold',
            color: 'primary.main',
            textShadow: '2px 2px 4px rgba(0,0,0,0.3)',
          }}
        >
          404
        </Typography>
        <Typography variant="h4" component="h2" gutterBottom color="text.secondary">
          Oups ! Page non trouvée
        </Typography>
        <Typography variant="body1" sx={{ mb: 4 }} color="text.secondary">
          Désolé, la page que vous cherchez semble avoir disparu dans le cyberespace.
        </Typography>
        <Button
          variant="contained"
          color="primary"
          size="large"
          startIcon={<HomeIcon />}
          onClick={handleReturn}
          sx={{
            borderRadius: '50px',
            padding: '10px 30px',
            fontSize: '1.1rem',
            transition: 'transform 0.3s ease-in-out',
            '&:hover': {
              transform: 'scale(1.05)',
            },
          }}
        >
          Retour à l'accueil
        </Button>
      </Box>
    </Container>
  )
}