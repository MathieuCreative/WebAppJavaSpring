<script>
import axios from 'axios';

export default {
  data() {
    return {
      images: [],
      selectedImageUrl: '',  // URL de l'image sélectionnée
      imageSURLs : [],
      file: '',
    };
  },
  methods: {
    fetchImages() {
      axios.get('http://localhost:4000/images')
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
      axios.get(`http://localhost:4000/images/${imageid}`, { responseType:"blob" })
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
    axios.get(`http://localhost:4000/images/${imageid}`, { responseType: "blob" })
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
  })
  .catch(function(){
    console.log('FAILURE!!');
  });
},
},
mounted() {
  this.fetchImages();
  //this.loadImage(0);
}
}
</script>


<template>
  <div>
    <h1>Liste des Images</h1>
    <ul>
      <li v-for="image in images" :key="image.id">
        {{ image.name }}
      </li>
    </ul>
  <img v-for="imageURL in imageSURLs" :src="imageURL" alt="Selected Image" />
    <select @change="loadImage">
      <option v-for="image in images" :key="image.name">
        {{ image.id }}
      </option>
    </select>
    <img v-if="selectedImageUrl" :src="selectedImageUrl" alt="Selected Image" />
  </div>
  <div class="container">
    <div>
      <h2>Single File</h2>
      <hr/>
      <label>File
        <input type="file" @change="handleFileUpload( $event )"/>
      </label>
      <br>
      <button v-on:click="submitFile()">Submit</button>
    </div>
  </div>
</template>



