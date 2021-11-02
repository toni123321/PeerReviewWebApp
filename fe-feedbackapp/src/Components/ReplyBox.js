import React, { Component } from "react"
import axios from "axios";

class ReplyBox extends Component {
  constructor(props) {
    super(props)
  }

  state = {
    comments: []
}


  handleSubmit(){
    axios.post('/create', {
      firstName: 'Fred',
      lastName: 'Flintstone'
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  render() {
    return (
      <div>
        <h1 className="title">Please leave your feedback below</h1>
        <form onSubmit={this.handleSubmit}>
          <div className="field">
            <div className="control">
              <input
                type="text"
                className="input"
                name="name"
                placeholder="Your name"
              />
            </div>
          </div>
          <div className="field">
            <div className="control">
              <textarea
                className="textarea"
                name="comment"
                placeholder="Add a comment"
              ></textarea>
            </div>
          </div>
          <div className="field">
            <div className="control">
              <button className="button is-primary">Submit</button>
            </div>
          </div>
        </form>
      </div>
    )
  }
}

export default ReplyBox





// import React from "react";


// export default class Comments extends React.Component{
    

   

//     render(){
//         return(
//             <ul>
//                 {this.state.comments.map(comment => <li>{comment.text}</li>)}
//             </ul>
//         )
//     }
// }
