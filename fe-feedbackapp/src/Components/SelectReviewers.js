import React, {useState, useEffect} from 'react';
import {Form, Button, InputGroup, FormControl} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import SelectedReviewer from './SelectedReviewer';
import axios from 'axios';

const client = axios.create({
    baseURL: "http://localhost:8080/" 
});


function SelectReviewers({saveReviewers}) {
    const [started, setStarted] = useState(false);
    const [users, setUsers] = useState([]);

    const [input, setInput] = useState("");
    const [checkAddReviewer, setCheckAddReviewer] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [reviewers, setReviewers] = useState([]);
    const [reviewersIds, setReviewersIds] = useState([]);

    const handleInputChange = (e) => {
        var inputVal = e.target.value;
        setInput(inputVal);
    }

    useEffect(() => {
        retrieveUsers();
        console.log("here");
    }, []);

    useEffect(() => {
        console.log(users);
        const results = users.filter(user =>
          user.email.split("@")[0].toLowerCase().includes(input)
        );
        if(input !== ""){
            setSearchResults(results);
        }else{
            setSearchResults([]);
        }

    }, [input]);

    async function retrieveUsers() {
        const response = await client.get("/users");
        setUsers(response.data);
    }

    const handleClick = (user) => {
        setReviewers([...reviewers, user]);
        setReviewersIds([...reviewersIds, user.studentNr]);
        
        setUsers([
            ...users.filter(u => {
                return u.studentNr !== user.studentNr
            })
        ]);
        setInput("");
    }

    const handleRemoveReviewer = (reviewer) => {
        // Remove the chosen reviewer from the reviewer list
        setReviewers([
            ...reviewers.filter(r => {
                return r.studentNr !== reviewer.studentNr
            })
        ]);
        setReviewersIds([
            ...reviewersIds.filter(r => {
                return r !== reviewer.studentNr
            })
        ])
        // Add removed reviewer to people list
        setUsers([...users, reviewer]);
    }

    
    const addReviewers = () => {
        console.log(reviewersIds);
        saveReviewers(reviewersIds);
    }

    return (
        <>
        {/* {users.map((u) => (
            <div>{u.email}</div>
        ))} */}
         
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Add searchResults</Form.Label>
                <InputGroup className="mb-3">
                    <FormControl
                    
                    placeholder="Select searchResults..."
                    aria-label="Select searchResults..."
                    aria-describedby="basic-addon2"
                    onChange={handleInputChange}
                    value={input}
                    />
                    <InputGroup.Text id="basic-addon2">@student.fontys.nl</InputGroup.Text>
                </InputGroup>
            </Form.Group>
            {searchResults.map((user) => (
                <div key={user.studentNr} className="searchResults">
                    <div onClick={() => handleClick(user)}>{user.studentNr} {user.email}</div>
                </div>
            ))}

            <Button variant="secondary" onClick={addReviewers}>
                Save reviewers
            </Button>
          
        {reviewers.map((reviewer) => (
            <div key={reviewer.studentNr} className="reviewers">
                <SelectedReviewer reviewer={reviewer} handleRemoveReviewer={handleRemoveReviewer}/>
            </div>
        ))}

        </>
    );
}

export default SelectReviewers;