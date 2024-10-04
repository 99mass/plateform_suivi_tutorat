import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: { main: '#1976d2' },
        secondary: { main: '#dc004e' },
        background: { default: '#f5f5f5' },
    },
    typography: {
        fontFamily: 'Roboto, "Helvetica Neue", Arial, sans-serif',
    },
    components: {
        MuiCard: {
            styleOverrides: {
                root: {
                    transition: 'box-shadow 0.3s ease-in-out, transform 0.3s ease-in-out',
                    '&:hover': {
                        boxShadow: '0 8px 16px 0 rgba(0,0,0,0.2)',
                        transform: 'translateY(-4px)',
                    },
                },
            },
        },
    },
});

export default theme;