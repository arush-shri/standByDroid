import { X } from "lucide-react-native";
import { Modal, Pressable, ScrollView, Text, TouchableOpacity } from "react-native";
import { scale, StyleSheet } from "react-native-size-scaling";

const ViewPicker = ({visible, closeModal, setValue}) => {
    const selecteView = (val) => {
        setValue(val);
        closeModal();
    }
    return (
        <Modal 
        onRequestClose={closeModal}
        transparent visible={visible} 
        animationType="fade">
            <Pressable onPress={closeModal} style={styles.modalOverlay}>
                <TouchableOpacity 
                onPress={closeModal}
                style={styles.closeButton}>
                    <X size={scale(20)} color={'rgba(255,255,255,0.8)'} />
                </TouchableOpacity>
                <ScrollView
                showsVerticalScrollIndicator={false}
                style={styles.scroller}
                contentContainerStyle={styles.scrollContainer}
                >
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_1')}}>
                        <Text style={styles.text}>View 1</Text>
                    </Pressable>
                    
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_2')}}>
                        <Text style={styles.text}>View 2</Text>
                    </Pressable>
                    
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_3')}}>
                        <Text style={styles.text}>View 3</Text>
                    </Pressable>
                    
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_4')}}>
                        <Text style={styles.text}>View 4</Text>
                    </Pressable>
                    
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_5')}}>
                        <Text style={styles.text}>View 5</Text>
                    </Pressable>
                    
                    <Pressable
                    style={styles.buttons}
                    onPress={() => {selecteView('view_6')}}>
                        <Text style={styles.text}>View 6</Text>
                    </Pressable>
                </ScrollView>
            </Pressable>
        </Modal>
    );
}

const styles = StyleSheet.create({
    modalOverlay: {
        flex: 1,
        backgroundColor: "rgba(0,0,0,0.5)",
        justifyContent: "center",
        alignItems: "center",
    },
    closeButton:{
        padding: 8,
        backgroundColor: 'rgba(255,255,255,0.3)',
        borderRadius: 10000,
        position: 'absolute',
        top: 20,
        right: 20,
        zIndex: 10
    },
    buttons: {
        backgroundColor: 'rgba(255,255,255,0.2)',
        width: '100%',
        paddingVertical: 10,
        borderWidth: 1.5,
        borderColor: 'rgba(255,255,255,0.5)',
        justifyContent: "center",
        alignItems: "center",
        borderRadius: 17,
        marginVertical: 6
    },
    text: {
        color: 'rgba(255,255,255,0.8)',
        fontSize: 20
    },
    scrollContainer: {
        justifyContent: "center", 
        alignItems: "center", 
        paddingVertical: 20,
    },
    scroller: {
        flex: 1, 
        width: '50%', 
        height: '100%',
    }
});

export default ViewPicker