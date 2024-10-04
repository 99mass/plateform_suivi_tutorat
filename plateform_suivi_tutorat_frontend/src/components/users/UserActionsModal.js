import React from 'react';
import { Modal, Fade, Box, Typography, Button } from '@mui/material';

const UserActionsModal = ({ open, onClose, onModify, onDelete, selectedUsers }) => {
  return (
    <Modal
      open={open}
      onClose={onClose}
      closeAfterTransition
    >
      <Fade in={open}>
        <Box sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: 400,
          bgcolor: 'background.paper',
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}>
          <Typography variant="h6" component="h2" gutterBottom>
            Que souhaitez-vous faire ?
          </Typography>
          <Typography variant="body2" sx={{ mt: 2 }}>
            {selectedUsers.length} utilisateur(s) sélectionné(s)
          </Typography>
          <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
            <Button onClick={onModify} variant="contained" color="primary">
              Modifier
            </Button>
            <Button onClick={onDelete} variant="contained" color="secondary">
              Supprimer
            </Button>
          </Box>
        </Box>
      </Fade>
    </Modal>
  );
};

export default UserActionsModal;
