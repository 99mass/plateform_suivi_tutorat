// // src/pages/ResponsiveAppBar.js
import React, { useCallback, useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import { Logout, ManageAccounts } from '@mui/icons-material';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import sessionService from '../services/sessionService';
import { useNavigate } from 'react-router-dom';

const ResponsiveAppBar = ({ user, onProfileClick, handlePath }) => {
  const [anchorElUser, setAnchorElUser] = useState(null);
  const navigate = useNavigate();

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleProfile = useCallback(() => {
    if (onProfileClick) {
      onProfileClick();
    } else {
      navigate('/profile');
    }
    handleCloseUserMenu();
  }, [navigate, onProfileClick]);

  const handleLogout = useCallback(() => {
    sessionService.endSession();
    navigate('/');
    handleCloseUserMenu();
  }, [navigate]);


  return (
    <AppBar position="fixed" sx={{ backgroundColor: '#fff', boxShadow: '0px 2px 4px -1px rgba(0,0,0,0.2)' }}>
      <Container maxWidth="xl">
        <Toolbar disableGutters sx={{ justifyContent: 'space-between' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={handlePath}>
            <img src="/assets/logo.png" alt="Logo" style={{ height: '80px', marginRight: '10px' }} />
          </Box>

          <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
              <Button
                onClick={handleOpenUserMenu}
                sx={{
                  color: 'primary.main',
                  textTransform: 'none',
                  '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.04)' }
                }}
                endIcon={<KeyboardArrowDownIcon />}
              >
                <Avatar sx={{ width: 32, height: 32, mr: 1 }} alt={user &&`${user.prenom?.charAt(0).toUpperCase() + user.prenom?.slice(1).toLowerCase()} ${user.nom?.charAt(0).toUpperCase() + user.nom?.slice(1).toLowerCase()}`} src="/static/images/avatar/2.jpg" />
                {user != null ? `${user.prenom?.charAt(0).toUpperCase() + user.prenom?.slice(1).toLowerCase()} ${user.nom?.charAt(0).toUpperCase() + user.nom?.slice(1).toLowerCase()}` : ""}
              </Button>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              <MenuItem onClick={handleProfile}>
                <ManageAccounts sx={{ mr: 1 }} />
                <Typography textAlign="center">Profile</Typography>
              </MenuItem>
              <MenuItem onClick={handleLogout}>
                <Logout sx={{ mr: 1 }} />
                <Typography textAlign="center">Logout</Typography>
              </MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default ResponsiveAppBar;