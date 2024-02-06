<script>
import axios from 'axios';

export default {
  data() {
    return {
      images: [],
      selectedImageUrl: '/images/0',  // URL de l'image sélectionnée
      imageSURLs : [],
      file: '',
      selectedTab: 'tab1', 
    };
  },
  methods: {
    fetchImages() {
      //axios.get('http://localhost:4000/images')
      axios.get('/images')
        .then(response => {
          this.images = response.data;
          this.loadImageS();  // Appel ici après avoir chargé les images
        })
        .catch(error => {
          console.error("There was an error!", error);
        });
    },
    loadImage(event) {
      const imageid = event.target.value; // Utilisez la valeur sélectionnée
      axios.get(`/images/${imageid}`, { responseType:"blob" })
      .then(response => {
    // Créer une URL pour le blob
    const url = URL.createObjectURL(response.data);
    // Mettre à jour l'URL de l'image dans le composant Vue
    this.selectedImageUrl = url;
  })
  .catch(error => {
    console.error("There was an error fetching the image:", error);
  });

    },
    loadImageS() {
  this.images.forEach((image) => {  // Utilisation de forEach pour itérer sur chaque image
    const imageid = image.id;  // Accès correct à l'id de l'image
    axios.get(`/images/${imageid}`, { responseType: "blob" })
      .then(response => {
        const url = URL.createObjectURL(response.data);
        this.imageSURLs.push(url);  // Ajoute l'URL à la liste
      })
      .catch(error => {
        console.error("There was an error fetching the image:", error);
      });
  });
},

handleFileUpload(event){
  this.file = event.target.files[0];
  },
submitFile(){
    let formData = new FormData();
    formData.append('file', this.file);

    //Ajouter une évaluation ici sur le file pour vérif qu'il contient bien une valeur
    axios.post( '/images',
    formData,
    {
      headers: {
          'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function(){
    console.log('SUCCESS!!');
    window.location.reload(); // Rafraîchir la page
  })
  .catch(function(){
    console.log('FAILURE!!');
  });
  this.images.push
},
},
mounted() {
  this.fetchImages();
  //this.loadImage(0);
}
}
</script>


<template>
<div class="logo-container">
  <div class="logo-icon">&#x1F4F7;</div> <!-- Icône d'appareil photo en caractère Unicode -->
  <div class="logo-text">Pictures Lab</div>
</div>
    <div>
    <button @click="selectedTab = 'tab1'">See a picture</button>
    <button @click="selectedTab = 'tab2'">View all pictures</button>
    <button @click="selectedTab = 'tab3'">Add a picture</button>

    <div v-if="selectedTab === 'tab1'">
      <h3>Pictures list</h3>
    <ul>
      <li v-for="image in images" :key="image.id">
        {{ image.name }}
      </li>
    </ul> 
    <div class = "containers">
    <select @change="loadImage">
      <option v-for="image in images" :key="image.name">
        {{ image.id }}
      </option>
    </select>
  </div>
    <img v-if="selectedImageUrl" :src="selectedImageUrl" alt="Selected Image" />
  </div>
    
  <div v-if="selectedTab === 'tab2'">
    <div class="images-container">
  <img v-for="imageURL in imageSURLs" :src="imageURL" alt="Selected Image" />
  </div>    
</div>

    <div v-if="selectedTab === 'tab3'">
      <div class="container">
      <h2>Single File</h2>
      <hr/>
      <label>File
        <input type="file" @change="handleFileUpload( $event )"/>
      </label>
      <br>
      <button v-on:click="submitFile()">Submit</button>
    </div>
</div>
  </div>
</template>



<style>

.logo-container {
  display: flex;
  align-items: center;
  color: white;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 40px; 
}

.logo-icon {
  font-size: 50px; /* Taille de l'icône */
  margin-right: 15px; /* Espacement entre l'icône et le texte */
}

.logo-text {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
  font-size: 40px; /* Taille du texte */
}

img {
  display: block; /* Pour éviter d'éventuels problèmes d'espacement */
  max-height: 300px; /* Limite la hauteur maximale à 300px */
  max-width: 300px; /* Limite la largeur maximale à 300px */
  width: auto; /* Garde le ratio d'aspect original de l'image */
  height: auto; /* Garde le ratio d'aspect original de l'image */
  display: block; /* Assure que l'image est affichée comme un bloc */
  margin: 0 auto; /* Centre l'image horizontalement si elle est plus petite que l'espace disponible */
  border-radius: 15px; /* Applique des bordures arrondies */
  object-fit: contain; /* Assure que l'image est entièrement visible, réduite si nécessaire, sans être coupée */
  box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* Optionnel : Ajoute une ombre légère pour un effet de profondeur */
}

.containers {
  display: flex;
  flex-direction: row; /* Stack les éléments verticalement */
  /*align-items: center; Centre les éléments horizontalement */
}

button {
  background-color: #000080; /* Bleu nuit */
  color: white; /* Texte blanc */
  margin: 10px; /* Ajoute de l'espace autour des boutons */
  padding: 10px 20px; /* Ajoute de l'espace à l'intérieur des boutons, 10px pour le haut/bas et 20px pour les côtés */
  border: none; /* Retire la bordure du bouton */
  border-radius: 5px; /* Adoucit les coins des boutons */
  cursor: pointer; /* Change le curseur en main quand on survole le bouton */
  transition: background-color 0.3s; /* Effet de transition pour le survol */
  margin-bottom: 50px; 

}

button:hover {
  background-color: #0000cd; /* Une teinte de bleu un peu plus claire pour le survol */
}

.images-container {
  display: flex; /* Utilise Flexbox pour organiser les éléments */
  flex-wrap: wrap; /* Permet aux éléments de passer à la ligne suivante si nécessaire */
  gap: 10px; /* Espacement entre les images */
  justify-content: flex-start; /* Alignement des enfants à gauche du conteneur */
}



/* Les styles globaux */
body {
  background-color: #1e1e1e; /* Couleur de fond gris foncé */
  color: white; /* Couleur de texte blanc pour tout le texte */
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
}

h1, h2, h3, h4, h5, h6 {
  color: white; /* S'assure que tous les titres sont également blancs */
}



</style>


