import Toast from 'react-native-simple-toast';

export const ToastMaker = (text, duration = 'short') => {
    Toast.show(text, duration==='long'? Toast.LONG : Toast.SHORT);
}