import React, { useState } from 'react';
import { View, Button, StyleSheet, Modal } from 'react-native';
import { getAuth, updatePassword, updateProfile } from 'firebase/auth';

const ProfileMenu = ({ handleLogout }) => {
  const [modalVisible, setModalVisible] = useState(false);

  const handleChangePassword = async () => {
    try {
      const auth = getAuth();
      await updatePassword(auth.currentUser, 'nueva-contraseña');
      console.log('Contraseña cambiada exitosamente');
      setModalVisible(false);
    } catch (error) {
      console.error('Error al cambiar la contraseña:', error.message);
    }
  };

  const handleChangeProfileImage = async () => {
    try {
      const auth = getAuth();
      await updateProfile(auth.currentUser, { photoURL: 'url-de-la-nueva-foto' });
      console.log('Foto de perfil cambiada exitosamente');
      setModalVisible(false);
    } catch (error) {
      console.error('Error al cambiar la foto de perfil:', error.message);
    }
  };

  return (
    <View style={styles.container}>
      <Button title="☰" onPress={() => setModalVisible(true)} />
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(false)}
      >
        <View style={styles.modalContainer}>
          <Button title="Cambiar contraseña" onPress={handleChangePassword} />
          <Button title="Cambiar foto de perfil" onPress={handleChangeProfileImage} />
          <Button title="Cerrar sesión" onPress={handleLogout} color="#e74c3c" />
          <Button title="Cerrar" onPress={() => setModalVisible(false)} />
        </View>
      </Modal>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  modalContainer: {
    marginTop: 100,
    padding: 20,
    backgroundColor: '#fff',
    borderRadius: 8,
  },
});

export default ProfileMenu;
