import React from "react";
import Table from "../../commons/tables/table";


const columns = [
        {
            Header: 'ID',
            accessor: 'id',
        },
        {
            Header: 'Name',
            accessor: 'name',
        },
        {
            Header: 'Role',
            accessor: 'role',
        },
        {
            Header: 'Username',
            accessor: 'username',
        },
       
];

const filters = [
    {
        accessor: 'name',
    }
];

const backgroundPage = {
    background: 'rgba(255, 255, 255, 0.8)', // Semi-transparent white background
    padding: '20px',
    borderRadius: '8px',
    width: '800px',
    margin: '0 auto',
    marginTop: '50px',
    textAlign: 'center',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)', // Box shadow for depth
  };

class PersonTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            tableData: this.props.tableData
        };
    }

    render() { 
        return ( 
            <Table style={backgroundPage}
                data={this.state.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
            />
        )
    }
}

export default PersonTable;